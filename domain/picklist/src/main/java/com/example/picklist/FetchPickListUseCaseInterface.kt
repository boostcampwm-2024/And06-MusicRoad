package com.example.picklist

import com.example.model.Pick

interface FetchPickListUseCaseInterface {
    suspend operator fun invoke(userId: String): Result<List<Pick>>
}
