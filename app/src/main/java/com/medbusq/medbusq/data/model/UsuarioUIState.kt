package com.medbusq.medbusq.data.model

data class UsuarioUIState (
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
    val terminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
){
}