package com.squirtles.domain.picklist

import com.squirtles.domain.model.Pick

interface FetchPickListUseCaseInterface {
    suspend operator fun invoke(userId: String): Result<List<Pick>>
}
