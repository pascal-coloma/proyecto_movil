package com.medbusq.medbusq.data.remote.dto

import com.medbusq.medbusq.data.model.Medicamento

class MedicamentoDto (
    val id:Int,
    val nombre:String,
    val laboratorio:String,
    val presentacion:String,
    val formaFarmaceutica: String
){
}
