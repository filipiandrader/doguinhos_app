package com.doguinhos_app.core

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
interface BasePresenter<in V> {

    fun attach(view: V)

    fun detach()
}