package org.koin.compiler.adapter

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class KotlinReleaseVersionTest {

    private fun v(s: String) = KotlinReleaseVersion.parseOrNull(s) ?: error("unparseable: $s")

    @Test
    fun parsesStableVersions() {
        val version = v("2.3.20")
        assertEquals(2, version.major)
        assertEquals(3, version.minor)
        assertEquals(20, version.patch)
        assertEquals(KotlinReleaseVersion.Maturity.STABLE, version.maturity)
    }

    @Test
    fun parsesPreReleaseQualifiers() {
        assertEquals(KotlinReleaseVersion.Maturity.BETA, v("2.4.0-Beta1").maturity)
        assertEquals(KotlinReleaseVersion.Maturity.RC, v("2.4.0-RC2").maturity)
        assertEquals(KotlinReleaseVersion.Maturity.DEV, v("2.4.20-dev-835").maturity)
        assertEquals(KotlinReleaseVersion.Maturity.SNAPSHOT, v("2.4.255-SNAPSHOT").maturity)
    }

    @Test
    fun rejectsGarbage() {
        assertNull(KotlinReleaseVersion.parseOrNull("unknown"))
        assertNull(KotlinReleaseVersion.parseOrNull(""))
        assertNull(KotlinReleaseVersion.parseOrNull("kotlin"))
    }

    @Test
    fun ordersByLineThenMaturity() {
        assertTrue(v("2.3.20") < v("2.4.0"))
        assertTrue(v("2.3.21") < v("2.4.0-Beta1"))
        assertTrue(v("2.4.0-Beta1") < v("2.4.0-RC1"))
        assertTrue(v("2.4.0-RC1") < v("2.4.0"))
        assertTrue(v("2.4.0") < v("2.4.20-dev-835"))
    }

    @Test
    fun preReleaseSelectsItsOwnLine() {
        // A 2.4.0 pre-release carries the 2.4 compiler ABI: it must select the
        // 2.4.0 adapter, not fall back to 2.3.20.
        assertTrue(v("2.4.0-Beta1").lineAtLeast(v("2.4.0")))
        assertTrue(v("2.4.0-dev-2124").lineAtLeast(v("2.4.0")))
        assertFalse(v("2.3.21").lineAtLeast(v("2.4.0")))
    }

    @Test
    fun floorBoundary() {
        assertTrue(v("2.3.20").lineAtLeast(v("2.3.20")))
        assertTrue(v("2.3.21").lineAtLeast(v("2.3.20")))
        assertFalse(v("2.3.10").lineAtLeast(v("2.3.20")))
        assertFalse(v("2.3.0").lineAtLeast(v("2.3.20")))
    }

    @Test
    fun patchReleaseSelectsSameLineAdapter() {
        // 2.4.10 (first 2.4 patch release) sits above the 2.4.0 registry entry and below
        // nothing else — selection must resolve to the 2.4 adapter, not warn as unknown.
        assertTrue(v("2.4.10").lineAtLeast(v("2.4.0")))
        assertTrue(v("2.4.10").lineAtLeast(v("2.3.20")))
        assertFalse(v("2.4.0").lineAtLeast(v("2.4.10")))
    }

    @Test
    fun registryDeclaresKotlin2410OnK240Adapter() {
        // Selection math over the REAL registry resource (no adapter classloading — that part
        // is exercised end-to-end by the main plugin's box tests): a 2.4.10 compiler must
        // resolve to the k240 adapter entry, and because 2.4.10 is declared in the registry it
        // is the newest entry — so the "newer than the newest tested version" warning cannot fire.
        val resource = KotlinAdapterLoader::class.java.classLoader
            .getResourceAsStream("META-INF/koin/kotlin-version-adapters.properties")
            ?: error("adapter registry resource missing")
        val properties = java.util.Properties().apply { resource.use { load(it) } }
        val registry = properties.entries
            .mapNotNull { (key, value) -> KotlinReleaseVersion.parseOrNull(key.toString())?.let { it to value.toString() } }
            .sortedBy { it.first }

        val current = v("2.4.10")
        val selected = registry.lastOrNull { current.lineAtLeast(it.first) }
            ?: error("no adapter entry selected for 2.4.10")
        assertEquals("org.koin.compiler.adapter.k240.Kotlin240Adapter", selected.second)
        val newest = registry.last()
        assertTrue(newest.first.lineAtLeast(current), "2.4.10 must be within the tested range (no newer-than-tested warning)")
    }
}
