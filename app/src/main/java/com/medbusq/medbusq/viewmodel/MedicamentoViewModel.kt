package com.medbusq.medbusq.viewmodel

import androidx.lifecycle.ViewModel
import com.medbusq.medbusq.model.MedicamentoErrores
import com.medbusq.medbusq.model.MedicamentoUIState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MedicamentoViewModel : ViewModel() {

    private val _estado = MutableStateFlow(MedicamentoUIState())

    val estado : StateFlow<MedicamentoUIState> = _estado

    fun onNombreChange(valor : String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun validarFormulario():Boolean{
        val estadoActual = _estado.value
        val errores = MedicamentoErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Debe ingresar el Nombre" else null,
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }
}