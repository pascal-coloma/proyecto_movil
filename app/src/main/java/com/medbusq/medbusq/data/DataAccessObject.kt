package com.medbusq.medbusq.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsusarioDao{
    @Query("SELECT * FROM usuario")
    fun getAll(): List<Ususario>

    @Insert
    fun insertAl(vararg usuarios: Ususario)
}