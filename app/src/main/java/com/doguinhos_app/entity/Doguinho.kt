package com.doguinhos_app.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.doguinhos_app.database.DataConverter

/**
 * Created by Filipi Andrade Rocha on 06/11/2019
 */
@Entity(tableName = "doguinhos")
@TypeConverters(DataConverter::class)
class Doguinho(@PrimaryKey
               var nome: String = "",
               var imagem: String = "",
               var favotiro: Boolean = false,
               var sub_raca: MutableList<String> = mutableListOf())

object DoguinhoSingleton {
    val instance = Doguinho()
}