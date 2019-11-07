package com.doguinhos_app.service

import com.doguinhos_app.entity.Doguinho
import com.doguinhos_app.util.nullOnEmptyConverterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
open class DogAPI {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://dog.ceo/api/")
        .addConverterFactory(nullOnEmptyConverterFactory)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    protected val retrofitService: DogEndpoints = retrofit.create(DogEndpoints::class.java)
}

interface DogEndpoints {

    // DOGS
    @GET("breeds/list/all")
    fun getAllDogs(): Deferred<HashMap<String, Any>>

    @GET("breed/{doguinho}/images")
    fun getImagesByDog(@Path("doguinho") doguinho: String): Deferred<HashMap<String, Any>>


}