package com.medbusq.medbusq.data

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            val newInstance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "medbusq_database"
            ).build()
            instance = newInstance
            newInstance
        }
    }

    fun closeDatabase() {
        instance?.close()
        instance = null
    }
}