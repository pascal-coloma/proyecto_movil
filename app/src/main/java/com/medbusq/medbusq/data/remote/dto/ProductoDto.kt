package com.medbusq.medbusq.data.remote.dto

import com.medbusq.medbusq.data.model.Medicamento

class ProductoDto(
    val id: Int,
    val nombreMedicamento: String,
    val laboratorio: String,
    val presentacion: String,
    val formaFarmaceutica: String
) {

    fun ProductoDto.toModel(): Medicamento =
        Medicamento(
            id = id,
            nombreMedicamento = nombreMedicamento,
            laboratorio = laboratorio,
            presentacion = presentacion,
            formaFarmaceutica = formaFarmaceutica
        )
}

