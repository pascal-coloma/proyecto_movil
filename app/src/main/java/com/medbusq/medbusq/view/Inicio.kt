package com.medbusq.medbusq.view


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    navController: NavController)
    {

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                   Row (verticalAlignment = Alignment.CenterVertically){

                       Text("MEDI BUSQUEDA", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, letterSpacing = 1.sp  )
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

    ){
        innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            Button(onClick = {navController.navigate("Busqueda") }){
                Text("Buscar Medicamentos")
            }

            Button(onClick = {navController.navigate("Perfil")}) {
                Text("Perfil")
            }



        }

    }
}