package com.doguinhos_app.main.domain

import com.doguinhos_app.core.BaseInteractorListener
import com.doguinhos_app.entity.Doguinho

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
interface MainInteractor {

    fun getDoguinhos(listener: GetDoguinhosListener)
}

interface GetDoguinhosListener : BaseInteractorListener {

    fun onSuccess(doguinhos: MutableList<Doguinho>)

    fun onFailure()
}