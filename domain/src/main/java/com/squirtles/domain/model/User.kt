package com.squirtles.domain.model

data class User(
    val userId: String,
    val userName: String,
    val userProfileImage: String?,
    val myPicks: List<String>,
)
