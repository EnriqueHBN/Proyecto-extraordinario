package com.example.animalsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.animalsapp.navigation.Navigation
import com.example.animalsapp.ui.theme.AnimalsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimalsAppTheme {
                Navigation()
            }
        }
    }
}