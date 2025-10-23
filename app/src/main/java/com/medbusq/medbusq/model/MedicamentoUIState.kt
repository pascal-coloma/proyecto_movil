package com.medbusq.medbusq.model

data class MedicamentoUIState(
    val nombre: String = "",
    val detalles: String = "",
    val url: String = "",
    val errores : MedicamentoErrores = MedicamentoErrores()
) {
}