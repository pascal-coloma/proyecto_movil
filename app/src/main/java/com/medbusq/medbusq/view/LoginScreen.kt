package com.medbusq.medbusq.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.medbusq.medbusq.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: UsuarioViewModel
) {
    var rut by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    var rutError by remember { mutableStateOf<String?>(null) }
    var claveError by remember { mutableStateOf<String?>(null) }

    var mostrarError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    fun validarRut(input: String): String? =
        when {
            input.isBlank() ->
                "El RUT no puede estar vacío"
            !input.all { it.isDigit() } ->
                "El RUT debe contener solo números"
            input.length < 8 ->
                "El RUT debe tener 8 dígitos"
            input.length > 8 ->
                "El RUT no puede tener más de 8 dígitos"
            else -> null
        }

    fun validarClave(input: String): String? =
        when {
            input.isBlank() ->
                "La contraseña no puede estar vacía"
            input.length < 8 ->
                "La contraseña debe tener al menos 8 caracteres"
            else -> null
        }

    val primary = MaterialTheme.colorScheme.primary
    val surface = MaterialTheme.colorScheme.surface
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val errorColor = MaterialTheme.colorScheme.error

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        primary,
                        primary.copy(alpha = 0.7f),
                        surface
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(
                        color = onPrimary.copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = onPrimary,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Bienvenido a MEDBUSQ",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = onPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Text(
                text = "Inicia sesión para buscar medicamentos cercanos",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = onPrimary.copy(alpha = 0.9f)
                ),
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Iniciar sesión",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    )

                    OutlinedTextField(
                        value = rut,
                        onValueChange = {
                            rut = it
                            mostrarError = false
                            rutError = validarRut(rut)
                        },
                        label = { Text("RUT (8 dígitos)") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null
                            )
                        },
                        isError = rutError != null,
                        supportingText = {
                            rutError?.let { msg ->
                                Text(
                                    text = msg,
                                    color = errorColor,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(18.dp)
                    )

                    OutlinedTextField(
                        value = clave,
                        onValueChange = {
                            clave = it
                            mostrarError = false
                            claveError = validarClave(clave)
                        },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null
                            )
                        },
                        isError = claveError != null,
                        supportingText = {
                            claveError?.let { msg ->
                                Text(
                                    text = msg,
                                    color = errorColor,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(18.dp)
                    )

                    if (mostrarError) {
                        Text(
                            text = mensajeError,
                            color = errorColor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            rutError = validarRut(rut)
                            claveError = validarClave(clave)

                            if (rutError != null || claveError != null) {
                                mostrarError = true
                                mensajeError = "Corrige los errores del formulario"
                                return@Button
                            }

                            viewModel.iniciarSesion(
                                rut = rut,
                                clave = clave,
                                onSuccess = {
                                    navController.navigate("inicio") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onError = { error ->
                                    mostrarError = true
                                    mensajeError = error
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Ingresar")
                    }

                    TextButton(
                        onClick = { navController.navigate("RegistroScreen") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    ) {
                        Text("¿No tienes cuenta? Regístrate")
                    }
                }
            }
        }
    }
}
