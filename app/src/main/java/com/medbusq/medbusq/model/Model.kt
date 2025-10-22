package com.medbusq.medbusq.model

data class DatosUsuario(
    val nombre : String = "",
    val correo : String = "",
    val contrasenna : String = "",
    val terminos : Boolean = false,
    val errores : DatosErrores = DatosErrores()
)



data class DatosErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val contrasenna: String? = null
)