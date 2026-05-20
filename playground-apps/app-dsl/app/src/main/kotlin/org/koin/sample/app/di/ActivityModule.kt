package org.koin.sample.app.di

import android.util.Log
import androidx.activity.ComponentActivity
import org.koin.androidx.scope.dsl.activityScope
import org.koin.dsl.module
import org.koin.plugin.module.dsl.scoped

val activityModule = module {
    activityScope {
        scoped<ActivityTracker>()
    }
}

class ActivityTracker(private val activity: ComponentActivity) {
    fun trackScreen(name: String) {
        Log.d("ActivityTracker", "Screen viewed: $name in ${activity.localClassName}")
    }
}