package com.example.animalsapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.animalsapp.models.Animal
import com.example.animalsapp.network.RetrofitInstance

@Composable
fun AnimalListScreen() {
    val animals = remember { mutableStateOf<List<Animal>>(emptyList()) }

    LaunchedEffect(true) {
        animals.value = try {
            RetrofitInstance.api.getAnimals()
        } catch (e: Exception) {
            emptyList()
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(animals.value.size) { index ->
            val animal = animals.value[index]
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(animal.mainImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Text(text = animal.name, style = MaterialTheme.typography.h6)
            }
        }
    }
}