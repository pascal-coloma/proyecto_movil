package com.medbusq.medbusq.data.remote


import com.medbusq.medbusq.data.remote.dto.CiudadDto
import retrofit2.http.GET
import retrofit2.http.Path

interface CiudadApiService {

    @GET("medbusq/v1/ciudad")
    suspend fun obtenerCiudades(

    ): List<CiudadDto>

    @GET("medbusq/v1/ciudad/{id}")
    suspend fun obtenerCiudadPorId(
        @Path("id") id: Int
    ): CiudadDto

}