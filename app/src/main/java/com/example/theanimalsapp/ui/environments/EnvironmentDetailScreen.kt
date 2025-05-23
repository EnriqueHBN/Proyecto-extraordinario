package com.example.theanimalsapp.ui.environments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.theanimalsapp.data.ApiClient
import com.example.theanimalsapp.data.Animal
import com.example.theanimalsapp.data.Environment
import kotlinx.coroutines.delay

@Composable
fun EnvironmentDetailScreen(
    environmentId: String,
    onAnimalClick: (String) -> Unit
) {
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
            errorMessage = "Error al cargar datos: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFB1E693))
        }
    } else if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(errorMessage ?: "Error desconocido", color = Color.White)
        }
    } else {
        environment?.let { env ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF2C3E50))
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = env.image,
                    contentDescription = env.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.DarkGray, shape = RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = env.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB1E693)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = env.description,
                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Animales en este ambiente:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB1E693)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(animals.size) { index ->
                        AnimalItem(animal = animals[index], onClick = { onAnimalClick(animals[index].id) })
                    }
                }
            }
        }
    }
}

@Composable
fun AnimalItem(animal: Animal, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF34495E))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = animal.image,
                contentDescription = animal.name,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.Gray, shape = CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = animal.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1
            )
        }
    }
}

@Composable
fun AutoScrollingCarousel(images: List<String>, modifier: Modifier = Modifier) {
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            currentIndex = (currentIndex + 1) % images.size
        }
    }

    Box(modifier = modifier) {
        AsyncImage(
            model = images[currentIndex],
            contentDescription = "Galería",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.DarkGray, shape = RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    }
}
