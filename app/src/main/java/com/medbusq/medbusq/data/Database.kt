package com.medbusq.medbusq.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Ususario::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun UsusarioDao(): UsusarioDao
}