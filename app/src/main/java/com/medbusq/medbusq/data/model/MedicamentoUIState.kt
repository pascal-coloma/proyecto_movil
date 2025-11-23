package com.medbusq.medbusq.data.model

data class MedicamentoUIState(
    val nombre: String = "",
    val detalles: String = "",
    val url: String = "",
    val errores : MedicamentoErrores = MedicamentoErrores()
) {
}