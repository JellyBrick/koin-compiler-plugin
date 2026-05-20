package org.koin.sample.common.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.sample.common.Dispatcher
import org.koin.sample.common.NiaDispatchers

val dispatchersModule = module {
    single { create(::dispatcherIO) }
    single { create(::dispatcherDefault) }
    single { create(::coroutineScope) }
}

@Dispatcher(NiaDispatchers.IO)
fun dispatcherIO(): CoroutineDispatcher = Dispatchers.IO

@Dispatcher(NiaDispatchers.Default)
fun dispatcherDefault(): CoroutineDispatcher = Dispatchers.Default

fun coroutineScope(@Dispatcher(NiaDispatchers.Default) default: CoroutineDispatcher) =
    CoroutineScope(SupervisorJob() + default)


