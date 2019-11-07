package com.doguinhos_app.main.presentation

import com.doguinhos_app.core.BasePresenter

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
interface MainPresenter : BasePresenter<MainView> {

    fun getDoguinhos()
}