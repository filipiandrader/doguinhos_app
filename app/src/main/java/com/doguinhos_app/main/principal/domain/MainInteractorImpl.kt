package com.doguinhos_app.main.principal.domain

import com.doguinhos_app.entity.Doguinho
import com.doguinhos_app.service.DogAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
class MainInteractorImpl : MainInteractor, DogAPI() {

    override fun getDoguinhos(listener: GetDoguinhosListener) {
        GlobalScope.launch {
            try {
                val result = retrofitService.getAllDogs().await()
                val message = JSONObject(result).optJSONObject("message")

                val doguinhos = mutableListOf<Doguinho>()
                message.keys().forEach { key ->
                    val doguinho = Doguinho()
                    doguinho.nome = key
                    val resultImage = retrofitService.getImagesRandomByDog(key).await()
                    doguinho.imagem = JSONObject(resultImage).optString("message")
                    val sub_raca = mutableListOf<String>()
                    for (i in 0 until message.optJSONArray(key).length()) {
                        sub_raca.add(message.optJSONArray(key).optString(i))
                    }
                    doguinho.sub_raca = sub_raca

                    doguinhos.add(doguinho)
                }

                GlobalScope.launch(Dispatchers.Main) { listener.onSuccess(doguinhos) }
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