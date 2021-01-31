package com.doguinhos_app.favorite.presentation

import android.content.Context
import com.doguinhos_app.core.BasePresenter

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
interface FavoritesPresenter : BasePresenter<FavoritesView> {

    fun getRacasFavoritas(context: Context)
}