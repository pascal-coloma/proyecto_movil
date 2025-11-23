package com.medbusq.medbusq.data.remote.dto

data class MedicamentoBusquedaRequest(
    val nombreMedicamento: String,
    val latitud: String,
    val longitud: String
)
