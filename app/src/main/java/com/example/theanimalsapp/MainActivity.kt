package com.example.theanimalsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.theanimalsapp.ui.animals.AnimalDetailScreen
import com.example.theanimalsapp.ui.animals.AnimalListScreen
import com.example.theanimalsapp.ui.environments.EnvironmentDetailScreen
import com.example.theanimalsapp.ui.environments.EnvironmentListScreen
import com.example.theanimalsapp.ui.theme.GreenPrimary
import com.example.theanimalsapp.ui.theme.TheAnimalsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheAnimalsApp()
        }
    }
}

@Composable
fun TheAnimalsApp() {
    TheAnimalsAppTheme {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "animalList",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("animalList") {
                    AnimalListScreen { animalId ->
                        println("🐾 Navegando con ID: $animalId")
                        navController.navigate("animalDetail/$animalId")
                    }
                }

                composable("animalDetail/{id}") { backStackEntry ->
                    val animalId = backStackEntry.arguments?.getString("id")
                    println("📦 Navegando a animal con ID: $animalId")
                    AnimalDetailScreen(animalId)
                }


                composable("environmentList") {
                    EnvironmentListScreen { environmentId ->
                        navController.navigate("environmentDetail/$environmentId")
                    }
                }

                composable("environmentDetail/{environmentId}") { backStackEntry ->
                    val environmentId = backStackEntry.arguments?.getString("environmentId")
                    if (environmentId != null) {
                        EnvironmentDetailScreen(
                            environmentId = environmentId,
                            onAnimalClick = { animalId ->
                                println("🐾 Navegando desde ambiente con ID: $animalId")
                                navController.navigate("animalDetail/$animalId")
                            }
                        )
                    } else {
                        Text("Error: ID del ambiente no válido")
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("Animales", "animalList", Icons.Default.Pets),
        NavigationItem("Ambientes", "environmentList", Icons.Default.Home)
    )

    NavigationBar(
        containerColor = GreenPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .shadow(8.dp, RoundedCornerShape(20.dp))
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label, style = MaterialTheme.typography.bodyMedium) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class NavigationItem(val label: String, val route: String, val icon: ImageVector)
