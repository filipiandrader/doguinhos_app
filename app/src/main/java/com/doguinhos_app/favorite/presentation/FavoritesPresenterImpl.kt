package com.doguinhos_app.favorite.presentation

import android.content.Context
import com.doguinhos_app.entity.Doguinho
import com.doguinhos_app.favorite.domain.FavoritesInteractor
import com.doguinhos_app.favorite.domain.GetRacasFavoritasListener

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
class FavoritesPresenterImpl(private val interactor: FavoritesInteractor,
                             private val context: Context) : FavoritesPresenter {

    private var mView: FavoritesView? = null

    override fun attach(view: FavoritesView) {
        mView = view
    }

    override fun getRacasFavoritas(context: Context) {
        mView?.showProgress()
        interactor.getRacasFavoritas(context, getRacasFavoritasListener)
    }

    private val getRacasFavoritasListener = object : GetRacasFavoritasListener {
        override fun onSuccess(doguinhos: MutableList<Doguinho>) {
            mView?.bindRacasFavoritas(doguinhos)
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