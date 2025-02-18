package com.example.firebase.model

data class FirebaseUser(
    val name: String? = null,
    val profileImage: String? = null,
    val myPicks: List<String> = emptyList()
)
