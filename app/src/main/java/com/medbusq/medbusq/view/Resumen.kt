package com.medbusq.medbusq.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.medbusq.medbusq.viewmodel.UsuarioViewModel

@Composable
fun ResumenScreen(viewModel: UsuarioViewModel){
    val estado by viewModel.estado.collectAsState()

    Column (
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.Top)
    {
        Text("Resumen de registro", style = MaterialTheme.typography.headlineMedium)
        Text("Nombre: ${estado.nombre}")
        Text("Correo: ${estado.correo}")
        Text("Ciudad: ${estado.ciudad}")
        Text("Contrasenna: ${"*".repeat(estado.clave.length)}")
        Text("Terminos aceptados?: ${if (estado.terminos) "Aceptados" else "Rechazados"}")
    }
}
