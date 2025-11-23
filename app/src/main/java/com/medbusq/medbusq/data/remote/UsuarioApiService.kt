package com.medbusq.medbusq.data.remote

import com.medbusq.medbusq.data.model.UsuarioUIState
import com.medbusq.medbusq.data.remote.dto.UsuarioDto
import retrofit2.Response
import retrofit2.http.*

interface UsuarioApiService {


    @GET("/medbusq/v1/usuario")
    suspend fun getUsuarios(): List<UsuarioDto>


    @GET("/medbusq/v1/usuario/{rut}")
    suspend fun getUsuario(@Path("rut") rut: String): UsuarioDto


    @POST("/medbusq/v1/usuario")
    suspend fun crearUsuario(@Body usuario: UsuarioUIState): Response<UsuarioDto>


    @PUT("/medbusq/v1/usuario/{rut}")
    suspend fun actualizarUsuario(
        @Path("rut") rut: String,
        @Body usuario: UsuarioDto
    ): Response<UsuarioDto>


    @DELETE("/medbusq/v1/usuario/{rut}")
    suspend fun eliminarUsuario(@Path("rut") rut: String): Response<Unit>
}