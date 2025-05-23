package com.example.animalsapp.models

data class Animal(
    val id: String,
    val name: String,
    val mainImage: String,
    val description: String,
    val gallery: List<String>,
    val facts: List<String>
)