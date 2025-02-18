package com.example.picklist

interface RemovePickUseCaseInterface {
    suspend operator fun invoke(pickId: String, userId: String): Result<Boolean>
}
