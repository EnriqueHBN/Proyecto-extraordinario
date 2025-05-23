package com.example.theanimalsapp.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// ✅ Animal con _id mapeado como id
data class Animal(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val image: String,
    val description: String,
    val imageGallery: List<String>?,
    val facts: List<String>?
)

// ✅ Environment con _id mapeado como id
data class Environment(
    @SerializedName("_id")
    val id: String,
    val name: String,
    val image: String,
    val description: String,
    val animals: List<Animal>
)

interface AnimalsApi {
    @GET("animals")
    suspend fun getAnimals(): List<Animal>

    @GET("environments")
    suspend fun getEnvironments(): List<Environment>

    @GET("animals/{id}")
    suspend fun getAnimalDetail(@Path("id") id: String): Animal

    @GET("environments/{id}")
    suspend fun getEnvironmentDetail(@Path("id") id: String): Environment

    @GET("animals")
    suspend fun getAnimalsByEnvironment(@Query("environmentId") environmentId: String): List<Animal>
}
