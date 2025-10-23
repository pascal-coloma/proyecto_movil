package com.medbusq.medbusq.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.medbusq.medbusq.model.MedicamentoUIState
import com.medbusq.medbusq.viewmodel.MedicamentoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Busqueda(
    navController: NavController,
    viewModel: MedicamentoViewModel
) {

    val estado by viewModel.estado.collectAsState()
    val resultados = viewModel.resultados
    val cargando = viewModel.cargando.value
    val ctx = LocalContext.current

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text("Buscar Medicamentos")
                },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
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
    ) {
        innerPadding ->
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
            Button(
                onClick = {
                    if (viewModel.validarFormulario()){
                        viewModel.buscarMedicamentos()
                    }
                },
                modifier = Modifier.fillMaxWidth()
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
                    Text("Buscando medicamentos")
                }
            }
            AnimatedVisibility(
                visible = !cargando && resultados.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LazyColumn (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    items(resultados.size){ index ->
                        val medicamento = resultados[index]
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column (modifier = Modifier.padding(16.dp)){
                                Text(
                                    text = medicamento.nombre,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = medicamento.detalles,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Column (modifier = Modifier.padding(16.dp)){
                                Button(onClick = {
                                    val urlIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(medicamento.url)
                                    )
                                    ctx.startActivity(urlIntent)
                                },
                                    modifier  = Modifier.fillMaxWidth())  {
                                    Text("Comprar")

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

