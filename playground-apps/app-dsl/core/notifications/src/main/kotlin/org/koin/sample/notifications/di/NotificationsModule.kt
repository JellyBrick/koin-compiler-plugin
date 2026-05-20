package org.koin.sample.notifications.di

import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory
import org.koin.sample.notifications.NoOpNotifier
import org.koin.sample.notifications.Notifier

val notificationsModule = module {
    factory<NoOpNotifier>() bind Notifier::class
}
