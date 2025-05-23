package com.example.theanimalsapp.ui.environments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.theanimalsapp.data.ApiClient
import com.example.theanimalsapp.data.Environment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnvironmentListScreen(onEnvironmentClick: (String) -> Unit) {
    var environments by remember { mutableStateOf<List<Environment>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        errorMessage = null
        try {
            environments = ApiClient.animalsApi.getEnvironments()
        } catch (e: Exception) {
            e.printStackTrace()
            errorMessage = "Error loading environments: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Environments", color = Color.White) },
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
                    environments.isEmpty() -> Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No environments found.", color = Color.Gray)
                    }
                    else -> LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(environments) { env ->
                            EnvironmentListItem(env) { onEnvironmentClick(env.id) }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun EnvironmentListItem(environment: Environment, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        // debug URL
        LaunchedEffect(environment.image) {
            println("🌐 List image URL = ${environment.image}")
        }
        AsyncImage(
            model = environment.image,
            contentDescription = environment.name,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = environment.name,
            style = MaterialTheme.typography.titleMedium.copy(color = Color.Black)
        )
        Text(
            text = environment.description.take(60) + "...",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.DarkGray)
        )
    }
}
