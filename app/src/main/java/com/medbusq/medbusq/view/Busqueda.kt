package com.medbusq.medbusq.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.medbusq.medbusq.viewmodel.MedicamentoViewModel
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuDefaults

import kotlin.math.exp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Busqueda(
    navController: NavController,
    viewModel: MedicamentoViewModel
) {

    val estado by viewModel.estado.collectAsState()
    val resultados by viewModel.resultados.collectAsState()
    val cargando = viewModel.cargando.value

    val ciudades by viewModel.ciudades.collectAsState()
    val ciudadSeleccionada by viewModel.ciudadSeleccionada.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    LaunchedEffect(ciudades) {
        println("CIUDADES RECIBIDAS: ${ciudades.size}")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Medicamentos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0x7500B4D4),
                    titleContentColor = Color.DarkGray,
                    navigationIconContentColor = Color.DarkGray
                ),
                windowInsets = WindowInsets.statusBars
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = viewModel::onNombreChange,
                label = { Text("Medicamento") },
                isError = estado.errores.nombre != null,
                supportingText = {
                    estado.errores.nombre?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { if (ciudades.isNotEmpty()) {expanded = !expanded} }
            ) {

                OutlinedTextField(
                    value = ciudadSeleccionada?.nombre ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Ciudad") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),

                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    ciudades.forEach { c ->
                        DropdownMenuItem(
                            text = { Text(c.nombre) },
                            onClick = {
                                viewModel.seleccionarCiudad(c)
                                expanded = false
                            }
                        )
                    }
                }
            }
            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                    viewModel.buscarMedicamentos()}
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text("Buscar")
            }

            AnimatedVisibility(
                visible = cargando,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Text("Buscando medicamentos...")
                }
            }

            if (!cargando) {
                if (resultados.isEmpty()) {
                    Text(
                        text = "No se encontraron medicamentos.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        items(
                            items = resultados,
                            key = { it.id }
                        ) { medicamento ->
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Nombre: ${medicamento.nombreMedicamento ?: "-"}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text("Laboratorio: ${medicamento.laboratorio ?: "-"}")
                                    Text("Presentación: ${medicamento.presentacion ?: "-"}")
                                    Text("Forma farmacéutica: ${medicamento.formaFarmaceutica ?: "-"}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
