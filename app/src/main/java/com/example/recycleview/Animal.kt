package com.example.recycleview

data class Animal(
    val nom: String,
    val espece: String, // Example: Mammifère, Oiseau, Reptile
    val imageResId: Int // Resource ID for the drawable image (e.g., R.drawable.chat)
)