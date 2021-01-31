package com.doguinhos_app.main.details.presentation

import com.doguinhos_app.main.details.domain.DetailsInteractor
import com.doguinhos_app.main.details.domain.GetDoguinhosImagensListener

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
class DetailsPresenterImpl(private val interactor: DetailsInteractor) : DetailsPresenter {

    private var mView: DetailsView? = null

    override fun attach(view: DetailsView) {
        mView = view
    }

    override fun getDoguinhosImagens(nome: String) {
        mView?.showProgress()
        interactor.getDoguinhosImagens(nome, getDoguinhosImagensListener)
    }

    private val getDoguinhosImagensListener = object : GetDoguinhosImagensListener {
        override fun onSuccess(doguinhosImagens: MutableList<String>) {
            mView?.bindDoguinhosImages(doguinhosImagens)
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