package com.medbusq.medbusq.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.medbusq.medbusq.model.MedicamentoErrores
import com.medbusq.medbusq.model.MedicamentoUIState
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MedicamentoViewModel : ViewModel() {

    private val baseMedicamentos = listOf(
        MedicamentoUIState("Paracetamol", "500mg, Tabletas"),
        MedicamentoUIState("Ibuprofeno", "200mg, Cápsulas"),
        MedicamentoUIState("Amoxicilina", "250mg, Suspensión"),
        MedicamentoUIState("Omeprazol", "20mg, Cápsulas"),
        MedicamentoUIState("Cetirizina", "10mg, Tabletas"),
        MedicamentoUIState("Loratadina", "10mg, Tabletas"),
        MedicamentoUIState("Metformina", "850mg, Tabletas"),
        MedicamentoUIState("Losartán", "50mg, Tabletas")
    )


    private val _estado = MutableStateFlow(MedicamentoUIState())
    private val _resultados = mutableStateListOf<MedicamentoUIState>()

    val estado : StateFlow<MedicamentoUIState> = _estado

    val resultados: List<MedicamentoUIState> = _resultados

    var cargando = mutableStateOf(false)
        private set
    fun onNombreChange(valor : String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun buscarMedicamentos(){
        val nombre = _estado.value.nombre.trim().lowercase()

        viewModelScope.launch {
            cargando.value = true

            delay(1000)
            val resultadosFiltrados = baseMedicamentos.filter {
                it.nombre.lowercase().contains(nombre)
            }

            _resultados.clear()
            _resultados.addAll(resultadosFiltrados)

            cargando.value = false
        }
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