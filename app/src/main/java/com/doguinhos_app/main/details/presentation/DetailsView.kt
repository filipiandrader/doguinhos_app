package com.doguinhos_app.main.details.presentation

import com.doguinhos_app.core.LoadingView
import com.doguinhos_app.core.MessageView

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
interface DetailsView : LoadingView, MessageView {

    fun bindDoguinhosImages(doguinhosImagens: MutableList<String>)
}