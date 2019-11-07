package com.doguinhos_app

import android.app.Application
import com.doguinhos_app.util.LogReportingTree
import timber.log.Timber

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
class DoguinhosApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(LogReportingTree())
        }
    }
}