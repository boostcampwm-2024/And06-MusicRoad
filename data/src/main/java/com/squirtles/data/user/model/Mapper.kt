package com.squirtles.data.user.model

import com.squirtles.domain.model.User

internal fun FirebaseUser.toUser(): User = User(
    userId = "",
    userName = name ?: "",
    userProfileImage = profileImage,
    myPicks = myPicks
)
