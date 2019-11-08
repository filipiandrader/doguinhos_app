package com.doguinhos_app.entity

class Doguinho(var nome: String = "",
               var imagem: String = "",
               var sub_raca: MutableList<String> = mutableListOf())

object DoguinhoSingleton {
    val instance = Doguinho()
}