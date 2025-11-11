package com.medbusq.medbusq.model

data class DatosUsuario(
    val nombre : String = "",
    val correo : String = "",
    val clave : String = "",
    val rut: String? = null,
    val terminos : Boolean = false,
    val errores : DatosErrores = DatosErrores()
)

data class DatosErrores(
    val nombre: String? = null,
    val rut: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val ciudad: String? = null
)