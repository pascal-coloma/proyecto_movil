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

open class MedicamentoViewModel : ViewModel() {

    private val baseMedicamentos = listOf(
        MedicamentoUIState("Paracetamol", "500mg, Comprimidos", url = "https://www.cruzverde.cl/paracetamol-500-mg-16-comprimidos/272241.html"),
        MedicamentoUIState("Paracetamol", "1gr, Comprimidos", url = "https://www.cruzverde.cl/xumadol-paracetamol-1000-mg-20-comprimidos/266145.html"),
        MedicamentoUIState("Ibuprofeno", "400mg, Cápsulas", url = "https://www.cruzverde.cl/ibuprofeno-400-mg-20-comprimidos/273441.html"),
        MedicamentoUIState("Ibuprofeno", "600mg, Cápsulas", url = "https://www.cruzverde.cl/ibuprofeno-600-mg-20-comprimidos/273362.html"),
        MedicamentoUIState("Amoxicilina", "875mg, Comprimidos", url = "https://www.cruzverde.cl/zolimax-duo-875125-amoxicilina-875-mg-20-comprimidos/292269.html"),
        MedicamentoUIState("Omeprazol", "20mg, Cápsulas", url = "https://www.cruzverde.cl/omeprazol-20-mg-30-capsulas-con-granulos/275886.html"),
        MedicamentoUIState("Cetirizina", "10mg, Comprimidos", url = "https://www.cruzverde.cl/remitex-cetirizina-10-mg-30-comprimidos-recubierto/9118.html"),
        MedicamentoUIState("Loratadina", "10mg, Comprimidos", url = "https://www.cruzverde.cl/loratadina-10-mg-30-comprimidos/273015.html"),
        MedicamentoUIState("Metformina", "750mg, Comprimidos", url = "https://www.cruzverde.cl/glicenex-sr--metformina-750-mg-30-comprimidos/260681.html"),
        MedicamentoUIState("Losartán", "50mg, Comprimidos", url = "https://www.cruzverde.cl/losartan-potasico-50-mg-30-comprimidos/268539.html")
    )

    private val _estado = MutableStateFlow(MedicamentoUIState())
    public val _resultados = mutableStateListOf<MedicamentoUIState>()

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

            delay(2000)
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