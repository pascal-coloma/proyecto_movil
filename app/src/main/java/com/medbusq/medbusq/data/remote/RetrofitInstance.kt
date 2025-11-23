package com.medbusq.medbusq.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL_MEDICAMENTOS = "http://54.161.22.247:3000/"
    private const val BASE_URL_USUARIOS = "http://54.161.22.247:8080/"

    val medicamentosApi: MedicamentoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_MEDICAMENTOS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MedicamentoApiService::class.java)
    }

    val usuariosApi: UsuarioApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_USUARIOS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioApiService::class.java)
    }
}
