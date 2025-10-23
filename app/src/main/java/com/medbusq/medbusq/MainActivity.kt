package com.medbusq.medbusq

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.medbusq.medbusq.view.Busqueda
import com.medbusq.medbusq.view.RegistroScreen
import com.medbusq.medbusq.view.ResumenScreen
import com.medbusq.medbusq.view.InicioScreen
import com.medbusq.medbusq.view.LoginScreen
import com.medbusq.medbusq.view.Perfil
import com.medbusq.medbusq.viewmodel.MedicamentoViewModel
import com.medbusq.medbusq.viewmodel.UsuarioViewModel
import com.medbusq.medbusq.viewmodel.UsuarioViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val usuarioViewModel: UsuarioViewModel = viewModel(
                factory = UsuarioViewModelFactory(application)
            )
            val medicamentoViewModel: MedicamentoViewModel = viewModel()

            NavHost(navController = navController, startDestination = "login"){
                composable("login") {
                    LoginScreen(
                        navController,
                        usuarioViewModel
                    )
                }
                composable("Inicio") {
                    InicioScreen(
                        navController
                    )
                }
                composable ("Busqueda"){
                    Busqueda(
                        navController,
                        medicamentoViewModel
                    )
                }
                composable("RegistroScreen") {
                    RegistroScreen(
                        navController, usuarioViewModel
                    )
                }
                composable ("resumen") {
                    ResumenScreen(
                        usuarioViewModel
                    )
                }
                composable ("Perfil"){
                    Perfil(
                        navController
                    )
                }
            }
        }
    }
}

