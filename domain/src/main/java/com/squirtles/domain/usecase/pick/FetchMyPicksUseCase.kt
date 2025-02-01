package com.squirtles.domain.usecase.pick

import com.squirtles.domain.remote.firebase.FirebasePickRepository
import com.squirtles.domain.usecase.picklist.FetchPickListUseCaseInterface
import javax.inject.Inject

class FetchMyPicksUseCase @Inject constructor(
    private val pickRepository: FirebasePickRepository
) : FetchPickListUseCaseInterface {
    override suspend operator fun invoke(userId: String) =
        pickRepository.fetchMyPicks(userId)
}
