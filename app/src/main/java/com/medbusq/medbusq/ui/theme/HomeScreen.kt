package com.medbusq.medbusq.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.intersect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.medbusq.medbusq.R
import java.util.function.IntConsumer


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(){
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    Scaffold (
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.logo_medbusq),
                                contentDescription = "Logo MediBusqueda",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .height(25.dp)
                            )
                            Text(
                                "MediBusqueda",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis)
                        }


                         }

                },
                navigationIcon = {
                    IconButton(onClick = {/* algo */}) {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "Flecha atras",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(25.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.menu_icon),
                            contentDescription = "Menu hamburguesa",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(25.dp)
                        )
                    }
                }

            )
        },
    ){ innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
        ){

        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}


