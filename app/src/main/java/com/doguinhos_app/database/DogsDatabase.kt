package com.doguinhos_app.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.doguinhos_app.entity.Doguinho

/**
 * Created by Filipi Andrade Rocha on 07/11/2019
 */
@Database(entities = [Doguinho::class],
    version = 1, exportSchema = false)
abstract class DogsDatabase : RoomDatabase() {

    abstract fun dogsDao(): DogsDAO

    companion object {
        private const val DB_NAME = "dogs.db"

        @Volatile
        private var INSTANCE: DogsDatabase? = null

        fun getInstance(context: Context) : DogsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: create(context).also { INSTANCE = it }
            }

        private fun create(context: Context) : DogsDatabase {
            return Room.databaseBuilder(context.applicationContext,
                DogsDatabase::class.java, DB_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}