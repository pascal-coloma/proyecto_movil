package com.medbusq.medbusq.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medbusq.medbusq.data.model.Medicamento
import com.medbusq.medbusq.data.model.MedicamentoErrores
import com.medbusq.medbusq.data.model.MedicamentoUIState
import com.medbusq.medbusq.data.remote.RetrofitInstance
import com.medbusq.medbusq.data.remote.dto.toModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MedicamentoViewModel : ViewModel() {

    // Estado del formulario/búsqueda
    private val _estado = MutableStateFlow(MedicamentoUIState())
    val estado: StateFlow<MedicamentoUIState> = _estado

    // Resultados de la API
    private val _resultados = MutableStateFlow<List<Medicamento>>(emptyList())
    val resultados: StateFlow<List<Medicamento>> = _resultados

    // Indicador de carga
    var cargando = mutableStateOf(false)
        private set

    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    init {
        // Cargar medicamentos al iniciar la pantalla
        buscarMedicamentos()
    }

    fun buscarMedicamentos() {
        viewModelScope.launch {
            cargando.value = true
            try {
                val dtoList = RetrofitInstance.medicamentosApi.getMedicamentos()
                val lista: List<Medicamento> = dtoList.map { it.toModel() }
                _resultados.value = lista
            } catch (e: Exception) {
                println("Error al obtener datos: ${e.localizedMessage}")
            } finally {
                cargando.value = false
            }
        }
    }

    fun validarFormulario(): Boolean {
        val estadoActual = _estado.value
        val errores = MedicamentoErrores(
            nombre = if (estadoActual.nombre.isBlank())
                "Debe ingresar el Nombre"
            else
                null
        )

        val hayErrores = listOfNotNull(
            errores.nombre
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }
}
