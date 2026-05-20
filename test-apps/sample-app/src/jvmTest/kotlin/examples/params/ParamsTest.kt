package examples.params

import org.junit.After
import org.junit.Test
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform
import org.koin.plugin.module.dsl.startKoin

class ParamsTest {

    @After
    fun tearDown() = stopKoin()

    @Test
    fun testInjectionParam(){

        startKoin<ParamsAppModule>()
        val koin = KoinPlatform.getKoin()
        val f1 = koin.get<MyFactory> { parametersOf("1","2") }
        assert(MyFactory("1","2") == f1)

        val f2 = koin.get<MyFactory2> { parametersOf("1",2) }
        assert(MyFactory2("1",2) == f2)
    }
}