package com.medbusq.medbusq.data.remote.dto

import com.medbusq.medbusq.data.model.Medicamento

// Request que envías al backend (equivalente al payload de JS)

// Cada elemento de "listado"
data class ListadoDto(
    val presentacionesExistentes: List<PresentacionDto>
)

fun ProductoDto.toModel(): Medicamento =
    Medicamento(
        id = id,
        nombreMedicamento = nombreMedicamento,
        laboratorio = laboratorio,
        presentacion = presentacion,
        formaFarmaceutica = formaFarmaceutica
    )


data class MedicamentoDto (
    val id:Int,
    val nombreMedicamento:String,
    val laboratorio:String,
    val presentacion:String,
    val formaFarmaceutica: String
){
}
