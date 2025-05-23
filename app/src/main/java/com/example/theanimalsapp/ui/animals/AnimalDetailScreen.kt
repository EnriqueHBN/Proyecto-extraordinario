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
fun AnimalDetailScreen(animalId: String?) {
    if (animalId.isNullOrBlank()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("❌ ID del animal no válido.", color = Color.Red)
        }
        return
    }

    var animal by remember { mutableStateOf<Animal?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(animalId) {
        println("🔍 Solicitando animal con ID: $animalId")
        isLoading = true
        try {
            animal = ApiClient.animalsApi.getAnimalDetail(animalId)
        } catch (e: Exception) {
            errorMessage = "No se pudo cargar el animal: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Animal", color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE0F7FA)
                )
            )
        },
        containerColor = Color(0xFFFDFDFD),
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                when {
                    isLoading -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFF2E7D32))
                    }
                    errorMessage != null -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = errorMessage!!, color = Color.Red)
                    }
                    else -> animal?.let { animalData ->
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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

                            item {
                                Text(
                                    text = animalData.name,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = Color.Black
                                )
                            }

                            item {
                                Text(
                                    text = animalData.description,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.DarkGray
                                )
                            }

                            if (!animalData.imageGallery.isNullOrEmpty()) {
                                item {
                                    Text(
                                        text = "Galería de Imágenes",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                                items(animalData.imageGallery) { imageUrl ->
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = "Imagen",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(150.dp)
                                            .clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            if (!animalData.facts.isNullOrEmpty()) {
                                item {
                                    Text(
                                        text = "Hechos Interesantes",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                                items(animalData.facts) { fact ->
                                    Text(
                                        text = "- $fact",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.DarkGray
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
