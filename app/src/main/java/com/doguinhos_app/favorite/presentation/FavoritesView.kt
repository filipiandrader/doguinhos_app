package com.doguinhos_app.favorite.presentation

import com.doguinhos_app.core.LoadingView
import com.doguinhos_app.core.MessageView
import com.doguinhos_app.entity.Doguinho

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
interface FavoritesView : LoadingView, MessageView {

    fun bindRacasFavoritas(doguinhos: MutableList<Doguinho>)
}