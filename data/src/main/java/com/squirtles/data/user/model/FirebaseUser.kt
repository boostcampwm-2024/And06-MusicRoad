package com.squirtles.data.user.model

import com.squirtles.domain.model.User

data class FirebaseUser(
    val name: String? = null,
    val profileImage: String? = null,
    val myPicks: List<String> = emptyList()
)
