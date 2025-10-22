package com.medbusq.medbusq.model

data class UsuarioUIState (
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val ciudad: String = "",
    val terminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores()
){
}