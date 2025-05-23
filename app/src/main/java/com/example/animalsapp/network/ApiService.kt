package com.example.animalsapp.network

import com.example.animalsapp.models.Animal
import retrofit2.http.GET

interface ApiService {
    @GET("animals")
    suspend fun getAnimals(): List<Animal>
}