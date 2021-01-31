package com.doguinhos_app.util

import timber.log.Timber

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
class LogReportingTree: Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Timber.d(message)
    }
}