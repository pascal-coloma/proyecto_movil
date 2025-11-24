package com.medbusq.medbusq.viewmodel

import android.app.Application
import android.util.Patterns
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
        val errorNombre =
            if (valor.isBlank() || valor.length < 3)
                "Nombre de longitud invalida"
            else
                null
        _estado.update { currentState ->
            currentState.copy(
                pnombre = valor,
                errores = currentState.errores.copy(nombre = errorNombre)
            )
        }
    }

    fun onSnombreChange(valor: String){
        val errorNombre =
            if (valor.isEmpty())
                null
            else if (valor.length < 3) {
                "Segundo nombre muy corto"
            } else {
                null
            }
        _estado.update { currentState ->
            currentState.copy(
                snombre = valor,
                errores = currentState.errores.copy(snombre = errorNombre)
            )
        }
    }

    fun onCorreoChange(valor: String) {
        val errorCorreo =
            when {
                valor.isBlank() ->
                    "El correo no puede estar vacío"
                !Patterns.EMAIL_ADDRESS.matcher(valor).matches() ->
                    "Correo no válido"
                else ->
                    null
            }
        _estado.update {
            it.copy(
                correo = valor,
                errores = it.errores.copy(correo = errorCorreo)
            )
        }
    }


    fun onClaveChange(valor: String) {
        val errorClave =
            when {
                valor.isBlank() ->
                    "La contraseña no puede estar vacía"
                valor.length < 8 ->
                    "La contraseña debe tener al menos 8 caracteres"
                else ->
                    null
            }
        _estado.update {
            it.copy(
                clave = valor,
                errores = it.errores.copy(clave = errorClave)
            )
        }
    }


    fun onCiudadChange(valor: String){
        _estado.update { it.copy(ciudad = valor, errores = it.errores.copy(ciudad = null))}
    }

    fun onTerminosChange(valor: Boolean){
        _estado.update { it.copy(terminos = valor) }
    }

    private fun validarRutModulo11(rutSinDv: String, dv: Char): Boolean {
        var factor = 2
        var suma = 0

        // Recorremos los dígitos de derecha a izquierda
        for (i in rutSinDv.length - 1 downTo 0) {
            val digito = rutSinDv[i] - '0'
            suma += digito * factor
            factor++
            if (factor > 7) factor = 2
        }

        val resto = suma % 11
        val resultado = 11 - resto

        val dvEsperado = when (resultado) {
            11 -> '0'
            10 -> 'K'
            else -> ('0' + resultado)
        }

        return dvEsperado == dv
    }


    fun onRutChange(valor: String) {
        val rut = valor.uppercase()
        val errorRut: String? = when {
            rut.isBlank() ->
                "Debe ingresar su RUT"
            !rut.matches(Regex("^[0-9K-]*$")) ->
                "Formato inválido: solo números, guion y K"
            rut.count { it == '-' } > 1 ->
                "Formato inválido: solo un guion"

            else -> {
                val partes = rut.split('-')

                when (partes.size) {
                    1 -> {
                        // Todavía no escribe el guion: deben ser solo dígitos, máximo 8
                        val cuerpo = partes[0]
                        when {
                            !cuerpo.matches(Regex("^\\d{0,8}$")) ->
                                "Los primeros dígitos deben ser números"

                            else -> null   // Hasta aquí, formato bien mientras escribe
                        }
                    }
                    2 -> {
                        val cuerpo = partes[0]
                        val dvStr = partes[1]

                        when {
                            !cuerpo.matches(Regex("^\\d{1,8}$")) ->
                                "Los primeros dígitos deben ser números"
                            dvStr.length > 1 ->
                                "El dígito verificador es solo un carácter"
                            dvStr.isEmpty() ->
                                null
                            !dvStr.matches(Regex("^[0-9K]$")) ->
                                "Dígito verificador inválido"
                            else -> {
                                val dvChar = dvStr[0]
                                val esValido = validarRutModulo11(cuerpo, dvChar)
                                if (!esValido) "RUT inválido"
                                else null
                            }
                        }
                    }

                    else -> "Formato inválido"
                }
            }
        }

        _estado.update {
            it.copy(
                rut = rut,
                errores = it.errores.copy(rut = errorRut)
            )
        }
    }




    fun validarFormulario():Boolean{
        val estadoActual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (estadoActual.pnombre.length < 3) "Nombre de longitud invalida" else null,
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

    fun iniciarSesion(rut: String, clave: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (rut.length < 8) {
                    onError("Rut no valido")
                    return@launch
                }
                if (clave.isBlank()) {
                    onError("La contraseña no puede estar vacía")
                    return@launch
                }

                // Obtener usuario del backend usando el repositorio
                val usuario = usuarioRepository.getUsuario(rut)
                
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