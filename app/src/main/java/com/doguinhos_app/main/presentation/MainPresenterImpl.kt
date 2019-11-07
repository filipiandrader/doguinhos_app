package com.doguinhos_app.main.presentation

import com.doguinhos_app.entity.Doguinho
import com.doguinhos_app.main.domain.GetDoguinhosListener
import com.doguinhos_app.main.domain.MainInteractor

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
class MainPresenterImpl(private val interactor: MainInteractor) : MainPresenter {

    private var mView: MainView? = null

    override fun attach(view: MainView) {
        mView = view
        getDoguinhos()
    }

    override fun getDoguinhos() {
        interactor.getDoguinhos(getDoguinhosListener)
    }

    private val getDoguinhosListener = object : GetDoguinhosListener {
        override fun onSuccess(doguinhos: MutableList<Doguinho>) {
            mView?.bindDoguinhos(doguinhos)
        }

        override fun onFailure() {
            mView?.hideProgress()
            mView?.showGenericErrorMessage()
        }

        override fun onInternetError() {
            mView?.hideProgress()
            mView?.showNoInternetMessage()
        }
    }

    override fun detach() {
        mView = null
    }
}