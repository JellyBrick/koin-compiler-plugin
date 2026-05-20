package org.koin.sample.app

import androidx.lifecycle.ViewModel
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ParamsViewModel(@InjectedParam val param1: String, @InjectedParam val param2: String) : ViewModel()

@KoinViewModel
class ParamsViewModel2(@InjectedParam val param1: String, @InjectedParam val param2: Int) : ViewModel()