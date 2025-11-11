package com.medbusq.medbusq.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    val rut: String,
    val dv_rut: String,
    val pnombre: String,
    val snombre: String?,
    val apaterno: String,
    val amaterno: String,
    val correo: String,
    val clave: String
)