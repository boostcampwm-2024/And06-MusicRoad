package com.example.pick.usecase

import com.example.pick.FirebasePickRepository
import com.example.picklist.FetchPickListUseCaseInterface
import javax.inject.Inject

class FetchMyPicksUseCase @Inject constructor(
    private val pickRepository: FirebasePickRepository
) : FetchPickListUseCaseInterface {
    override suspend operator fun invoke(userId: String) =
        pickRepository.fetchMyPicks(userId)
}
