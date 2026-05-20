//package org.koin.sample.app.di
//
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.koin.android.ext.koin.androidContext
//import org.koin.dsl.koinApplication
//import org.koin.sample.data.repository.OfflineFirstNewsRepository
//import org.koin.sample.domain.GetFollowableTopicsUseCase
//import org.robolectric.RobolectricTestRunner
//import org.robolectric.RuntimeEnvironment
//import kotlin.test.assertNotNull
//
//@RunWith(RobolectricTestRunner::class)
//class KoinTest {
//
//    // for debugging purpose only
//    @Test
//    fun testKoin() {
//
//        val koin = koinApplication {
//            androidContext(RuntimeEnvironment.getApplication())
//            modules(appModule)
//        }.koin
//
//        assertNotNull(koin.getOrNull<GetFollowableTopicsUseCase>())
//        assertNotNull(koin.getOrNull<OfflineFirstNewsRepository>())
//        koin.close()
//    }
//}
