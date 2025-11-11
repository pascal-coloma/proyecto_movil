package com.medbusq.medbusq.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.medbusq.medbusq.data.DatabaseProvider
import com.medbusq.medbusq.data.Usuario
import com.medbusq.medbusq.model.UsuarioErrores
import com.medbusq.medbusq.model.UsuarioUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.datastore.core.IOException
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.Request

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {
    private val PROFILE_IMAGE_KEY = "profile_image_uri"
    private val database = DatabaseProvider.getDatabase(application)
    private val usuarioDao = database.usuarioDao()
    private val sharedPreferences = application.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val _estado = MutableStateFlow(UsuarioUIState())

    private val client = OkHttpClient();

    fun run() {
        viewModelScope.launch(Dispatchers.IO) {
            val url = "http://10.0.2.2:8080/medbusq/v1/usuario"
            val request = Request.Builder().url(url).build()

            try {
                Log.d("UsuarioVM", "Intentando conectar con $url")
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        Log.e("UsuarioVM", "Error HTTP: ${response.code}")
                        return@use
                    }

                    val body = response.body?.string()
                    Log.d("UsuarioVM", "Respuesta exitosa: $body")
                }
            } catch (e: IOException) {
                Log.e("UsuarioVM", "Error de red: ${e.message}", e)
            } catch (e: Exception) {
                Log.e("UsuarioVM", "Error inesperado: ${e.message}", e)
            }
        }
    }

    val estado: StateFlow<UsuarioUIState> = _estado

    fun onNombreChange(valor: String){
        val error = if (valor.length < 3) "Nombre muy corto" else null
        _estado.update { it.copy(nombre = valor,errores = it.errores.copy(nombre = error)) }
    }

    fun onCorreoChange(valor: String){
        val error = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(valor).matches()) "Correo no valido" else null

        _estado.update { it.copy(
            correo = valor,
            errores = it.errores.copy(correo = error)) }
    }

    fun isPasswordValid(password: String): Boolean {
        return Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&._]{8,}\$")
            .matches(password)
    }
    fun onClaveChange(valor: String){
        val error = if (!isPasswordValid(valor)) "Contrasenna no valida" else null
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = error)) }
    }

    fun onCiudadChange(valor: String){
        _estado.update { it.copy(ciudad = valor, errores = it.errores.copy(ciudad = null))}
    }

    fun onTerminosChange(valor: Boolean){
        _estado.update { it.copy(terminos = valor) }
    }

    fun rutFormatoValido(rut: String): Boolean {
        val regex = Regex("^\\d{7,8}-[\\dkK]\$")
        if (!regex.matches(rut)) return false

        val (numero, dv) = rut.split("-")
        val rutNum = numero.toInt()

        // calculo dv algoritmo 11
        var suma = 0
        var multiplicador = 2

        for (i in numero.reversed()) {
            suma += (i.digitToInt() * multiplicador)
            multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
        }

        val resto = 11 - (suma % 11)
        val dvCalculado = when (resto) {
            11 -> "0"
            10 -> "K"
            else -> resto.toString()
        }

        return dv.equals(dvCalculado, ignoreCase = true)
    }
    fun onRutChange(valor: String){
        val error = if (!rutFormatoValido(valor)) "Rut no valido" else null
        _estado.update { it.copy(rut = valor, errores = it.errores.copy(rut = error)) }
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

                val usuario = usuarioDao.getByEmail(correo)
                if (usuario == null) {
                    onError("Usuario no encontrado")
                    return@launch
                }

                if (usuario.clave != clave) {
                    onError("Contraseña incorrecta")
                    return@launch
                }

                sharedPreferences.edit()
                    .putString("username", usuario.nombre)
                    .putString("userMail", usuario.correo)
                    .putString("userCiudad", usuario.ciudad)
                    .putString("userRut", usuario.rut)
                    .apply()

                _estado.update {
                    it.copy(
                        nombre = usuario.nombre,
                        correo = usuario.correo,
                        ciudad = usuario.ciudad,
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
                val usuarioExistente = usuarioDao.getByEmail(_estado.value.correo)
                if (usuarioExistente != null) {
                    onError("Ya existe un usuario con este correo electrónico")
                    return@launch
                }

                val nuevoUsuario = Usuario(
                    nombre = _estado.value.nombre,
                    correo = _estado.value.correo,
                    clave = _estado.value.clave,
                    ciudad = _estado.value.ciudad,
                    rut = _estado.value.rut
                )

                usuarioDao.insert(nuevoUsuario)
                onSuccess()
                val prefs = getApplication<Application>().getSharedPreferences("sesion", android.content.Context.MODE_PRIVATE)
                prefs.edit { putString("correo", _estado.value.correo) }
            } catch (e: Exception) {
                onError("Error al registrar usuario: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        DatabaseProvider.closeDatabase()
    }

    fun obtenerNombreUsuario(): String {
        return sharedPreferences.getString("username", "") ?: ""
    }

    fun obtenerCorreoUsuario(): String {
        return sharedPreferences.getString("userMail", "") ?: ""
    }

    fun obtenerciudadUsuario(): String {
        return sharedPreferences.getString("userCiudad", "") ?: ""
    }

    fun obtenerRunUsuario(): String {
        return sharedPreferences.getString("userRut", "") ?: ""
    }

    fun saveProfileImage(uri: String) {
        sharedPreferences.edit()
            .putString(PROFILE_IMAGE_KEY, uri)
            .apply()
    }

    fun getProfileImage(): String {
        return sharedPreferences.getString(PROFILE_IMAGE_KEY, "") ?: ""
    }

    fun cerrarSesion() {
        sharedPreferences.edit()
            .remove("user_id")
            .remove("user_name")
            .remove("user_email")
            .remove("user_ciudad")
            .remove("user_rut")
            .apply()
        _estado.update { UsuarioUIState() }
    }

    fun traerUsuario(correo: String){
        viewModelScope.launch {
            try {
                val usuario = usuarioDao.getByEmail(correo)
                if (usuario != null) {
                    _estado.update {
                        it.copy(
                            nombre = usuario.nombre,
                            correo = usuario.correo,
                            ciudad = usuario.ciudad,
                            rut = usuario.rut
                        )
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}