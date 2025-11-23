package com.medbusq.medbusq.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.medbusq.medbusq.data.model.UsuarioErrores
import com.medbusq.medbusq.model.UsuarioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.medbusq.medbusq.data.repository.UsuarioRepository
import com.medbusq.medbusq.data.model.UsuarioUIState as UsuarioUIStateDto
import com.medbusq.medbusq.data.remote.dto.UsuarioDto

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {
    private val usuarioRepository = UsuarioRepository()
    private val _estado = MutableStateFlow(UsuarioUIState())

    val estado: StateFlow<UsuarioUIState> = _estado

    fun onPnombreChange(valor: String){
        _estado.update { currentState ->
            currentState.copy(
                pnombre = valor,
                errores = currentState.errores.copy(nombre = null)
            )
        }
    }

    fun onSnombreChange(valor: String){
        _estado.update { currentState ->
            currentState.copy(
                snombre = valor,
                errores = currentState.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoChange(valor: String){
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String){
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onCiudadChange(valor: String){
        _estado.update { it.copy(ciudad = valor, errores = it.errores.copy(ciudad = null))}
    }

    fun onTerminosChange(valor: Boolean){
        _estado.update { it.copy(terminos = valor) }
    }

    fun onRutChange(valor: String){
        _estado.update { it.copy(rut = valor) }
    }

    fun validarFormulario():Boolean{
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.nombre.isBlank()) "Debe ingresar el Nombre" else null,
            correo = if (!estadoActual.correo.contains(("@"))) "Correo no valido" else null,
            clave = if  (estadoActual.clave.length < 8) "Contrasenna debe tener al menos 8 caracteres" else null,
            ciudad = if (estadoActual.ciudad.isBlank()) "Debe ingresar una ciudad" else null,
            rut = if (estadoActual.rut.isBlank()) "Debe ingresar su rut" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre,
            errores.correo,
            errores.clave,
            errores.ciudad,
            errores.rut
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }

        return !hayErrores
    }

    fun iniciarSesion(correo: String, clave: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (correo.isBlank()) {
                    onError("El correo no puede estar vacío")
                    return@launch
                }
                if (clave.isBlank()) {
                    onError("La contraseña no puede estar vacía")
                    return@launch
                }

                // Obtener usuario del backend usando el repositorio
                val usuario = usuarioRepository.getUsuario(correo)
                
                if (usuario.clave != clave) {
                    onError("Contraseña incorrecta")
                    return@launch
                }

                // Actualizar estado con datos del usuario
                _estado.update {
                    it.copy(
                        pnombre = usuario.pnombre,
                        snombre = usuario.snombre ?: "",
                        apaterno = usuario.apaterno,
                        amaterno = usuario.amaterno,
                        correo = usuario.correo,
                        rut = usuario.rut
                    )
                }
                onSuccess()
            } catch (e: Exception) {
                onError("Error al iniciar sesión: ${e.message}")
            }
        }
    }

    fun registrarUsuario(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!validarFormulario()) {
            onError("Por favor, corrija los errores en el formulario")
            return
        }

        viewModelScope.launch {
            try {

                val nuevoUsuario = UsuarioUIStateDto(
                    rut = _estado.value.rut,
                    dv_rut = _estado.value.dv_rut,
                    pnombre = _estado.value.pnombre,
                    snombre = _estado.value.snombre,
                    apaterno = _estado.value.apaterno,
                    amaterno = _estado.value.amaterno,
                    correo = _estado.value.correo,
                    clave = _estado.value.clave,
                    confirmClave = _estado.value.confirmClave,
                    ciudad = _estado.value.ciudad,
                    terminos = _estado.value.terminos,
                    errores = _estado.value.errores
                )

                val response = usuarioRepository.crearUsuario(nuevoUsuario)
                
                if (response.isSuccessful) {
                    // Actualizar estado con el usuario creado
                    val usuarioCreado = response.body()
                    if (usuarioCreado != null) {
                        _estado.update {
                            it.copy(
                                pnombre = usuarioCreado.pnombre,
                                snombre = usuarioCreado.snombre ?: "",
                                apaterno = usuarioCreado.apaterno,
                                amaterno = usuarioCreado.amaterno,
                                correo = usuarioCreado.correo,
                                rut = usuarioCreado.rut
                            )
                        }
                    }
                    onSuccess()
                } else {
                    onError("Error al registrar: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error al registrar usuario: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Limpieza de recursos si es necesaria
    }

    fun cerrarSesion() {
        _estado.update { UsuarioUIState() }
    }

    fun traerUsuario(correo: String){
        viewModelScope.launch {
            try {
                val usuario: UsuarioDto = usuarioRepository.getUsuario(correo)

                _estado.update {
                    it.copy(
                        pnombre = usuario.pnombre,
                        snombre = usuario.snombre ?: "",
                        apaterno = usuario.apaterno,
                        amaterno = usuario.amaterno,
                        correo = usuario.correo,
                        rut = usuario.rut
                    )
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}