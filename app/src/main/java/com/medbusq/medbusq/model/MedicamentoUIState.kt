package com.medbusq.medbusq.model

data class MedicamentoUIState(
    val nombre: String = "",
    val errores : MedicamentoErrores = MedicamentoErrores()
) {
}