package com.medbusq.medbusq.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Ususario(
    @PrimaryKey val run: Int,
    @ColumnInfo(name = "ususario")val nombre: String,
    @ColumnInfo(name = "correo")val correo: String,
    @ColumnInfo(name = "ciudad")val ciudad: Int
)