package com.doguinhos_app.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
class DataConverter {

    @TypeConverter
    fun toSubRacaList(string: String) : List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    fun fromSubRacaList(string: List<String>) : String {
        return Gson().toJson(string)
    }
}