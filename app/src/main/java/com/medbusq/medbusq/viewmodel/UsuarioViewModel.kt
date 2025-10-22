package com.medbusq.medbusq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.room.util.copy
import com.medbusq.medbusq.model.UsuarioErrores
import com.medbusq.medbusq.model.UsuarioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {
    private val _estado = MutableStateFlow(UsuarioUIState())

    val estado: StateFlow<UsuarioUIState> = _estado

    fun onNombreChange(valor: String){
        _estado.update { it.copy(nombre = valor,errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String){
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String){
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onCiudadChange(valor: String){
        _estado.update { it.copy(ciudad = valor, errores = it.errores.copy(ciudad = null))}
    }

    fun onTerminosChange(valor: Boolean){
        _estado.update { it.copy(terminos = valor) }
    }

    fun validarFormulario():Boolean{
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Debe ingresar el Nombre" else null,
            correo = if (!estadoActual.correo.contains(("@"))) "Correo no valido" else null,
            clave = if  (estadoActual.clave.length < 8) "Contrasenna debe tener al menos 8 caracteres" else null,
            ciudad = if (estadoActual.ciudad.isBlank()) "Debe ingresar una ciudad" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.ciudad
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }

}