package com.doguinhos_app.main.details.domain

import com.doguinhos_app.core.BaseInteractorListener

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
interface DetailsInteractor {

    fun getDoguinhosImagens(nome: String, listener: GetDoguinhosImagensListener)
}

interface GetDoguinhosImagensListener : BaseInteractorListener {

    fun onSuccess(doguinhosImagens: MutableList<String>)

    fun onFailure()
}