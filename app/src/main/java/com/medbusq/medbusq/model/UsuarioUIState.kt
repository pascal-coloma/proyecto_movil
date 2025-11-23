package com.medbusq.medbusq.model

import com.medbusq.medbusq.data.model.UsuarioErrores

data class UsuarioUIState(
    val rut: String = "",
    val dv_rut: String = "",
    val pnombre: String = "",
    val snombre: String = "",
    val apaterno: String = "",
    val amaterno: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmClave: String = "",
    val ciudad: String = "",
    val ciudadId: String = "",
    val terminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
){
    val nombre: String
        get() = listOf(pnombre, snombre, apaterno, amaterno).filter { it.isNotBlank() }.joinToString(" ")
}
