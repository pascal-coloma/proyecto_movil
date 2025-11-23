package com.medbusq.medbusq.data.repository

import com.medbusq.medbusq.data.model.UsuarioUIState
import com.medbusq.medbusq.data.remote.RetrofitInstance
import com.medbusq.medbusq.data.remote.dto.UsuarioDto
import retrofit2.Response



class UsuarioRepository {


    suspend fun getUsuarios(): List<UsuarioDto> {
        return try {
            RetrofitInstance.usuariosApi.getUsuarios()
        } catch (e: Exception) {
            throw Exception("Error al obtener usuarios: ${e.message}")
        }
    }


    suspend fun getUsuario(rut: String): UsuarioDto {
        return try {
            if (rut.isBlank()) {
                throw IllegalArgumentException("El RUT no puede estar vacío")
            }
            RetrofitInstance.usuariosApi.getUsuario(rut)
        } catch (e: Exception) {
            throw Exception("Error al obtener usuario con RUT $rut: ${e.message}")
        }
    }


    suspend fun crearUsuario(usuario: UsuarioUIState): Response<UsuarioDto> {
        return try {
            if (usuario.correo.isBlank() || usuario.clave.isBlank()) {
                throw IllegalArgumentException("El correo y contraseña son obligatorios")
            }
            RetrofitInstance.usuariosApi.crearUsuario(usuario)
        } catch (e: Exception) {
            throw Exception("Error al crear usuario: ${e.message}")
        }
    }

    suspend fun actualizarUsuario(rut: String, usuario: UsuarioDto): Response<UsuarioDto> {
        return try {
            if (rut.isBlank()) {
                throw IllegalArgumentException("El RUT no puede estar vacío")
            }
            RetrofitInstance.usuariosApi.actualizarUsuario(rut, usuario)
        } catch (e: Exception) {
            throw Exception("Error al actualizar usuario: ${e.message}")
        }
    }


    suspend fun eliminarUsuario(rut: String): Response<Unit> {
        return try {
            if (rut.isBlank()) {
                throw IllegalArgumentException("El RUT no puede estar vacío")
            }
            RetrofitInstance.usuariosApi.eliminarUsuario(rut)
        } catch (e: Exception) {
            throw Exception("Error al eliminar usuario: ${e.message}")
        }
    }
}