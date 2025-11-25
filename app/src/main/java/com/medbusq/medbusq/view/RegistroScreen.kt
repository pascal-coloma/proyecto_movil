package com.medbusq.medbusq.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.medbusq.medbusq.viewmodel.UsuarioViewModel

@Composable
fun RegistroScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
){
    val estado by viewModel.estado.collectAsState()


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

        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = onPrimary
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
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
                text = "Registro",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = onPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 8.dp, bottom = 16.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    OutlinedTextField(
                        value = estado.pnombre,
                        onValueChange = viewModel::onPnombreChange,
                        label = { Text("Nombre") },
                        isError = estado.errores.nombre != null,
                        supportingText = {
                            estado.errores.nombre?.let {
                                Text(it, color = errorColor)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(18.dp)
                    )

                    OutlinedTextField(
                        value = estado.snombre,
                        onValueChange = viewModel::onSnombreChange,
                        label = { Text("Segundo nombre") },
                        isError = estado.errores.snombre != null,
                        supportingText = {
                            estado.errores.snombre?.let {
                                Text(it, color = errorColor)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(18.dp)
                    )

                    OutlinedTextField(
                        value = estado.rut,
                        onValueChange = viewModel::onRutChange,
                        label = {Text("Ingrese RUT con guion y sin puntos")},
                        isError = estado.errores.rut != null,
                        supportingText = {
                            estado.errores.rut?.let{
                                Text(it, color = errorColor)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(18.dp)
                    )

                    OutlinedTextField(
                        value = estado.correo,
                        onValueChange = viewModel::onCorreoChange,
                        label = {Text("Correo")},
                        isError = estado.errores.correo != null,
                        supportingText = {
                            estado.errores.correo?.let{
                                Text(it, color = errorColor)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(18.dp)
                    )

                    OutlinedTextField(
                        value = estado.clave,
                        onValueChange = viewModel::onClaveChange,
                        label = {Text("Clave")},
                        visualTransformation = PasswordVisualTransformation(),
                        isError = estado.errores.clave != null,
                        supportingText = {
                            estado.errores.clave?.let {
                                Text(it, color = errorColor)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(18.dp)
                    )

                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ){
                        Checkbox(
                            checked = estado.terminos,
                            onCheckedChange = viewModel::onTerminosChange
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Acepto los terminos y condiciones")
                    }

                    var mostrarError by remember { mutableStateOf(false) }
                    var mensajeError by remember { mutableStateOf("") }

                    if (mostrarError) {
                        Text(
                            text = mensajeError,
                            color = errorColor,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            viewModel.registrarUsuario(
                                onSuccess = {
                                    navController.navigate("login") {
                                        popUpTo("RegistroScreen") { inclusive = true }
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
                        Text("Registrar")
                    }
                }
            }
        }
    }
}