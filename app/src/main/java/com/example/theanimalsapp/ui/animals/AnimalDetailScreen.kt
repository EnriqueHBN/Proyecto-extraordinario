package com.example.theanimalsapp.ui.animals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailScreen(animalId: String) {
    var animal by remember { mutableStateOf<Animal?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Cargar datos desde la API
    LaunchedEffect(animalId) {
        isLoading = true
        errorMessage = null
        try {
            animal = ApiClient.animalsApi.getAnimalDetail(animalId)
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = "No se pudo cargar el animal: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Animal") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C3E50),
                    titleContentColor = Color(0xFFB1E693)
                )
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFF2C3E50))
            ) {
                when {
                    isLoading -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color(0xFFB1E693))
                    }
                    errorMessage != null -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage ?: "Error desconocido",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    else -> animal?.let { animalData ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Imagen principal
                            item {
                                AsyncImage(
                                    model = animalData.image,
                                    contentDescription = animalData.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            // Nombre
                            item {
                                Text(
                                    text = animalData.name,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB1E693)
                                    )
                                )
                            }

                            // Descripción
                            item {
                                Text(
                                    text = animalData.description,
                                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.White)
                                )
                            }

                            // Galería de imágenes
                            if (!animalData.imageGallery.isNullOrEmpty()) {
                                item {
                                    Text(
                                        text = "Galería de Imágenes",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFFB1E693)
                                        )
                                    )
                                }
                                items(animalData.imageGallery) { imageUrl ->
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = "Imagen de galería",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            // Lista de hechos interesantes
                            if (!animalData.facts.isNullOrEmpty()) {
                                item {
                                    Text(
                                        text = "Hechos Interesantes",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFFB1E693)
                                        )
                                    )
                                }
                                items(animalData.facts) { fact ->
                                    Text(
                                        text = "- $fact",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
