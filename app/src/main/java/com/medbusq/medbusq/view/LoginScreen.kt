package com.medbusq.medbusq.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.medbusq.medbusq.viewmodel.UsuarioViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: UsuarioViewModel
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var mostrarError by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = correo,
            onValueChange = { 
                correo = it
                mostrarError = false
            },
            label = { Text("Correo electrónico") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = clave,
            onValueChange = { 
                clave = it
                mostrarError = false
            },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        if (mostrarError) {
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Button(
            onClick = {
                viewModel.iniciarSesion(
                    correo = correo,
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
                .padding(vertical = 16.dp)
        ) {
            Text("Iniciar Sesión")
        }

        TextButton(
            onClick = { navController.navigate("RegistroScreen") }
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
        Button(onClick = { viewModel.run() }) {
            Text("Datos")
        }
    }
}