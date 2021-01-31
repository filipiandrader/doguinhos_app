package com.doguinhos_app.main.details.presentation

import com.doguinhos_app.core.BasePresenter

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
interface DetailsPresenter : BasePresenter<DetailsView> {

    fun getDoguinhosImagens(nome: String)
}