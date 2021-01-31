package com.doguinhos_app.database

import androidx.room.*
import com.doguinhos_app.entity.Doguinho

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */

@Dao
abstract class DogsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg doguinho: Doguinho)

    @Delete
    abstract fun delete(doguinho: Doguinho)

    @Query("SELECT * FROM doguinhos ORDER BY nome")
    abstract fun getDoguinhos() : List<Doguinho>

    @Query("SELECT * FROM doguinhos WHERE nome = :nome ORDER BY nome")
    abstract fun getDoguinho(nome: String) : Doguinho?
}