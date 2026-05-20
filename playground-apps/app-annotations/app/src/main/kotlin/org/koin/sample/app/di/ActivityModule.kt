package org.koin.sample.app.di

import android.util.Log
import androidx.activity.ComponentActivity
import org.koin.android.annotation.ActivityScope
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Provided
import org.koin.sample.data.repository.NewsRepository

@Module
@Configuration
class ActivityModule {

    @ActivityScope
    fun activityTracker(activity: ComponentActivity, @Provided mp : MyProvider, rep : NewsRepository): ActivityTracker =
        ActivityTracker(activity,mp,rep)
}

class MyProvider(val id : String = "")

class ActivityTracker(
    private val activity: ComponentActivity,
    private val mp: MyProvider,
    private val rep: NewsRepository
) {
    init {
        Log.d("ActivityTracker", "MyProvider '${mp.id}'")
    }
    fun trackScreen(name: String) {
        Log.d("ActivityTracker", "Screen viewed: $name in ${activity.localClassName}")
    }
}
