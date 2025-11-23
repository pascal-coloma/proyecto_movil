package com.medbusq.medbusq.data.remote

import com.medbusq.medbusq.data.model.Medicamento
import com.medbusq.medbusq.data.remote.dto.MedicamentoDto
import retrofit2.http.GET
import retrofit2.http.POST

interface MedicamentoApiService {
    @POST("/api/medicamento")
    suspend fun getMedicamentos(): List<MedicamentoDto>
}