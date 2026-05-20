package org.koin.sample.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.sample.app.di.ActivityTracker
import org.koin.sample.app.di.MyInj
import org.koin.sample.app.di.MyProvider
import org.koin.sample.app.ui.AppNavigation
import org.koin.sample.network.NetworkMonitor

class MainActivity : ComponentActivity(), AndroidScopeComponent {

    override val scope: Scope by activityScope()

    private val viewModel: MainActivityViewModel by viewModel()
    private val activityTracker: ActivityTracker by inject()
    private val networkMonitor: NetworkMonitor by inject()
    private val myInj  : MyInj by inject {parametersOf("42")}

    private val vmParams : ParamsViewModel by viewModel { parametersOf("1", "2") }
    private val vmParams2 : ParamsViewModel2 by viewModel { parametersOf("1", 2) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // For dynamic declared components
        scope.getKoin().declare(MyProvider("MainActivityProvider"))

        activityTracker.trackScreen("MainActivity")

        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
            AppNavigation(isDarkTheme = isDarkTheme)
        }

        println("vmParams:${vmParams.param1}:${vmParams.param2}, ${vmParams2.param1}:${vmParams2.param2}")
        assert("1" == vmParams.param1)
        assert("2" == vmParams.param2)
        assert("1" == vmParams2.param1)
        assert(2 == vmParams2.param2)
    }
}
