package com.medbusq.medbusq.data.model

data class MedicamentoUIState(
    val nombre: String = "",
    val latitud: String = "",
    val longitud: String= "",
    val errores : MedicamentoErrores = MedicamentoErrores()
) {
}