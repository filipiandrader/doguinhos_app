package com.doguinhos_app.favorite.domain

import android.content.Context
import com.doguinhos_app.database.DogsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
class FavoritesInteractorImpl : FavoritesInteractor {

    override fun getRacasFavoritas(context: Context, listener: GetRacasFavoritasListener) {
        GlobalScope.launch {
            try {
                val doguinhos = DogsDatabase.getInstance(context).dogsDao().getDoguinhos()
                GlobalScope.launch(Dispatchers.Main) { listener.onSuccess(doguinhos.toMutableList()) }
            } catch (e: Exception) {
                e.printStackTrace()
                GlobalScope.launch(Dispatchers.Main) { listener.onFailure() }
            } catch (io: IOException) {
                io.printStackTrace()
                GlobalScope.launch(Dispatchers.Main) { listener.onInternetError() }
            }
        }
    }
}