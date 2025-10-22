package com.medbusq.medbusq.viewmodel

import androidx.lifecycle.ViewModel
import com.medbusq.medbusq.model.DatosErrores
import com.medbusq.medbusq.model.DatosUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DatosViewModel : ViewModel(){

    private val _estado = MutableStateFlow(DatosUsuario())

    val estado: StateFlow<DatosUsuario> = _estado

    fun onNombreChange(valor: String){
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String){
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onPassChange(valor: String){
        _estado.update { it.copy(contrasenna = valor, errores = it.errores.copy(contrasenna = null)) }
    }

    fun onTerminosChange(valor: Boolean){
        _estado.update { it.copy(terminos = valor) }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = DatosErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Campo obligatorio" else null,
            correo = if (!estadoActual.correo.contains("@")) "Correo no valido" else null,
            contrasenna = if (estadoActual.contrasenna.length < 8) "Debe tener al menos 8 caracteres" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.contrasenna
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }

}

