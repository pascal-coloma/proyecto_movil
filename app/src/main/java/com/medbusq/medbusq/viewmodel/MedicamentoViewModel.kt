package com.medbusq.medbusq.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medbusq.medbusq.data.model.Medicamento
import com.medbusq.medbusq.data.model.MedicamentoErrores
import com.medbusq.medbusq.data.model.MedicamentoUIState
import com.medbusq.medbusq.data.remote.RetrofitInstance
import com.medbusq.medbusq.data.remote.dto.CiudadDto
import com.medbusq.medbusq.data.remote.dto.MedicamentoBusquedaRequest
import com.medbusq.medbusq.data.remote.dto.toModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MedicamentoViewModel : ViewModel() {

    private val _estado = MutableStateFlow(MedicamentoUIState())
    val estado: StateFlow<MedicamentoUIState> = _estado

    private val _resultados = MutableStateFlow<List<Medicamento>>(emptyList())
    val resultados: StateFlow<List<Medicamento>> = _resultados

    var cargando = mutableStateOf(false)
        private set

    private val _ciudades = MutableStateFlow<List<CiudadDto>>(emptyList())
    val ciudades: StateFlow<List<CiudadDto>> = _ciudades

    private val _ciudadSeleccionada = MutableStateFlow<CiudadDto?>(null)
    val ciudadSeleccionada: StateFlow<CiudadDto?> = _ciudadSeleccionada

    init {
        cargarCiudades()
    }
    fun cargarCiudades() {
        viewModelScope.launch {
            try {
                val dtoList: List<CiudadDto> =
                    RetrofitInstance.ciudadesApi.obtenerCiudades()
                _ciudades.value = dtoList
            } catch (e: Exception) {
                println("Error al obtener ciudades: ${e.localizedMessage}")
            }
        }
    }

    fun seleccionarCiudad(ciudad: CiudadDto) {
        _ciudadSeleccionada.value = ciudad
        // Actualizar latitud/longitud en el estado
        _estado.update {
            it.copy(
                latitud = ciudad.latitud,
                longitud = ciudad.longitud
            )
        }
    }

    fun onNombreChange(valor: String) {
        _estado.update {
            it.copy(
                nombre = valor,
                errores = it.errores.copy(nombre = null)
            )
        }
    }
    fun buscarMedicamentos() {
        val estadoActual = _estado.value
        val lat = estadoActual.latitud
        val lon = estadoActual.longitud

        if (!validarFormulario()) return

        if (lat.isBlank() || lon.isBlank()) {
            println("No hay ciudad seleccionada o lat/lon nulos")
            _resultados.value = emptyList()
            return
        }

        viewModelScope.launch {
            cargando.value = true
            try {
                val request = MedicamentoBusquedaRequest(
                    latitud = lat,
                    longitud = lon,
                    nombreMedicamento = estadoActual.nombre.uppercase()
                )

                // Llamada al backend
                val respuesta = RetrofitInstance.medicamentosApi.getMedicamentos(request)

                val productos = mutableListOf<Medicamento>()

                respuesta.listado.forEach { item ->
                    item.presentacionesExistentes.forEach { presentacion ->
                        presentacion.productos.forEach { prod ->
                            productos += prod.toModel()
                        }
                    }
                }

                _resultados.value = productos

            } catch (e: Exception) {
                println("Error al obtener datos: ${e.localizedMessage}")
                _resultados.value = emptyList()
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

        val hayErrores = listOfNotNull(errores.nombre).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }
}
