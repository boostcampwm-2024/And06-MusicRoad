package com.squirtles.domain.usecase.picklist

import com.squirtles.domain.firebase.FirebaseRepository
import com.squirtles.domain.model.Pick

abstract class FetchPickListUseCaseTemplate(
    val firebaseRepository: FirebaseRepository
) {
    abstract suspend operator fun invoke(userId: String): Result<List<Pick>>
}
