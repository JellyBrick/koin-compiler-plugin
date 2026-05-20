package org.koin.sample.app.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan("org.koin.sample.feature")
class FeaturesModule
