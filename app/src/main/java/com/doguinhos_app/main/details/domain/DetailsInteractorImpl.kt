package com.doguinhos_app.main.details.domain

import com.doguinhos_app.service.DogAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
class DetailsInteractorImpl : DetailsInteractor, DogAPI() {

    override fun getDoguinhosImagens(nome: String, listener: GetDoguinhosImagensListener) {
        GlobalScope.launch {
            try {
                val result = retrofitService.getImagesByDog(nome).await()
                val message = JSONObject(result).optJSONArray("message")

                val doguinhosImagens = mutableListOf<String>()

                for (i in 0 until message.length()) {
                    doguinhosImagens.add(message.optString(i))
                }

                GlobalScope.launch(Dispatchers.Main) { listener.onSuccess(doguinhosImagens) }
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