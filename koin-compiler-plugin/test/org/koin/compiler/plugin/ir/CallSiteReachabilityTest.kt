package org.koin.compiler.plugin.ir

import java.lang.reflect.Proxy
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CallSiteReachabilityTest {
    private val validator = CallSiteValidator(
        Proxy.newProxyInstance(
            IrPluginContext::class.java.classLoader,
            arrayOf(IrPluginContext::class.java),
        ) { _, method, _ -> error("Reachability must not access compiler context: ${method.name}") } as IrPluginContext
    )

    @Test
    fun `converging includes and cycles preserve all reachable modules`() {
        val graph = mapOf(
            "root" to listOf("left", "right", "left"),
            "left" to listOf("shared"),
            "right" to listOf("shared"),
            "shared" to listOf("root"),
            "unused" to listOf("missing"),
        )

        assertEquals(
            setOf("root", "left", "right", "shared"),
            validator.computeReachableModules(listOf("root", "root"), graph, null),
        )
    }

    @Test
    fun `dense converging graph visits every module once`() {
        val parents = List(300) { "parent$it" }
        val leaves = List(300) { "leaf$it" }
        val edges = buildMap {
            put("root", parents)
            for (parent in parents) put(parent, leaves)
        }
        val reads = mutableMapOf<String, Int>()
        val graph = object : Map<String, List<String>> by edges {
            override fun get(key: String): List<String>? {
                reads[key] = reads.getOrDefault(key, 0) + 1
                return edges[key]
            }
        }

        val reachable = validator.computeReachableModules(listOf("root"), graph, null)

        assertEquals((listOf("root") + parents + leaves).toSet(), reachable)
        assertEquals(reachable!!.associateWith { 1 }, reads)
    }

    @Test
    fun `only a reachable incomplete module makes the graph unverifiable`() {
        val graph = mapOf("root" to listOf("child"))

        assertEquals(
            setOf("root", "child"),
            validator.computeReachableModules(listOf("root"), graph, null, modulesWithIncompleteIncludes = setOf("unused")),
        )
        assertNull(validator.computeReachableModules(listOf("root"), graph, null, modulesWithIncompleteIncludes = setOf("child")))
        assertNull(validator.computeReachableModules(emptyList(), graph, null, entryModulesIncomplete = true))
    }
}
