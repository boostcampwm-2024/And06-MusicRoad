package com.squirtles.domain.usecase.picklist

import com.squirtles.domain.firebase.FirebaseRepository

abstract class DeletePickListUseCaseTemplate(
    val firebaseRepository: FirebaseRepository
) {
    abstract suspend operator fun invoke(pickId: String, userId: String): Result<Boolean>
}
