package com.squirtles.data.datasource.remote.firebase.model

data class FirebaseUser(
    val name: String? = null,
    val myPicks: List<String> = emptyList()
)
