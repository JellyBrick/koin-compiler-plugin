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
import org.koin.core.scope.Scope
import org.koin.sample.app.di.ActivityTracker
import org.koin.sample.app.ui.AppNavigation
import org.koin.sample.network.NetworkMonitor

class MainActivity : ComponentActivity(), AndroidScopeComponent {

    override val scope: Scope by activityScope()

    private val viewModel: MainActivityViewModel by viewModel()
    private val activityTracker: ActivityTracker by inject()
    private val networkMonitor: NetworkMonitor by inject()
//    private val myInj  : MyInj by inject {parametersOf("42")}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTracker.trackScreen("MainActivity")

        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()
            AppNavigation(isDarkTheme = isDarkTheme)
        }
    }
}
