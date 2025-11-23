package com.medbusq.medbusq.data.repository

import com.medbusq.medbusq.data.model.Medicamento
import com.medbusq.medbusq.data.remote.RetrofitInstance
import com.medbusq.medbusq.data.remote.dto.MedicamentoDto

class MedicamentoRepository {

    suspend fun getMedicamentos(): List<MedicamentoDto>{
        return RetrofitInstance.medicamentosApi.getMedicamentos();
    }
}