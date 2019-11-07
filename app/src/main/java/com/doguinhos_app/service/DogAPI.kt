package com.doguinhos_app.service

import com.doguinhos_app.util.nullOnEmptyConverterFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
class DogAPI {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://dog.ceo/api/")
        .addConverterFactory(nullOnEmptyConverterFactory)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    protected val retrofitService: DogEndpoints = retrofit.create(DogEndpoints::class.java)
}

interface DogEndpoints {



}