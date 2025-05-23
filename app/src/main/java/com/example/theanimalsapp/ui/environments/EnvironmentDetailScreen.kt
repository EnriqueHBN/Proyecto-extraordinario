package com.example.theanimalsapp.ui.environments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.theanimalsapp.data.ApiClient
import com.example.theanimalsapp.data.Animal
import com.example.theanimalsapp.data.Environment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnvironmentDetailScreen(
    environmentId: String?,
    onAnimalClick: (String) -> Unit
) {
    if (environmentId.isNullOrBlank()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Invalid Environment ID", color = Color.Red)
        }
        return
    }

    var environment by remember { mutableStateOf<Environment?>(null) }
    var animals by remember { mutableStateOf<List<Animal>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(environmentId) {
        isLoading = true
        errorMessage = null
        try {
            environment = ApiClient.animalsApi.getEnvironmentDetail(environmentId)
            animals = ApiClient.animalsApi.getAnimalsByEnvironment(environmentId)
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = "Error loading environment: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(environment?.name ?: "Environment Detail", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2196F3))
            )
        },
        containerColor = Color(0xFFF5F5F5),
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    isLoading -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFF2196F3))
                    }
                    errorMessage != null -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = errorMessage!!, color = Color.Red)
                    }
                    else -> environment?.let { env ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            // debug URL
                            LaunchedEffect(env.image) {
                                println("🌐 Environment image URL = ${env.image}")
                            }
                            AsyncImage(
                                model = env.image,
                                contentDescription = env.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = env.name,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = env.description,
                                style = MaterialTheme.typography.bodyLarge.copy(color = Color.DarkGray)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Animals in this environment:",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2196F3)
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            if (animals.isEmpty()) {
                                Text("No animals found.", color = Color.Gray)
                            } else {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(animals) { animal ->
                                        EnvironmentAnimalItem(animal = animal, onClick = onAnimalClick)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun EnvironmentAnimalItem(animal: Animal, onClick: (String) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
            .clickable { onClick(animal.id) }
            .background(Color.White, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        AsyncImage(
            model = animal.image,
            contentDescription = animal.name,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = animal.name,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
            maxLines = 1
        )
    }
}
