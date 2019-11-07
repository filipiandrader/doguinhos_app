package com.doguinhos_app.main.principal.presentation

import com.doguinhos_app.core.LoadingView
import com.doguinhos_app.core.MessageView
import com.doguinhos_app.entity.Doguinho

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
interface MainView : LoadingView, MessageView {

    fun bindDoguinhos(doguinhos: MutableList<Doguinho>)

    fun showEmptyMessage()

    fun hideEmptyMessage()
}