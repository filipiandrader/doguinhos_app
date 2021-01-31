package com.doguinhos_app.favorite.domain

import android.content.Context
import com.doguinhos_app.core.BaseInteractorListener
import com.doguinhos_app.entity.Doguinho

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
interface FavoritesInteractor {

    fun getRacasFavoritas(context: Context, listener: GetRacasFavoritasListener)
}

interface GetRacasFavoritasListener : BaseInteractorListener {

    fun onSuccess(doguinhos: MutableList<Doguinho>)

    fun onFailure()
}