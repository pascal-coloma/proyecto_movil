package com.medbusq.medbusq.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL_MEDICAMENTOS = "http://54.161.22.247:3000/"
    private const val BASE_URL_USUARIOS = "http://54.161.22.247:8080/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // <-- Muestra todo
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    val medicamentosApi: MedicamentoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_MEDICAMENTOS)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MedicamentoApiService::class.java)
    }

    val usuariosApi: UsuarioApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_USUARIOS)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioApiService::class.java)
    }

    val ciudadesApi: CiudadApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://54.161.22.247:8080/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CiudadApiService::class.java)
    }
}
