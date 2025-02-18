package com.example.order.usecase

import com.example.order.LocalPickListOrderRepository
import com.example.picklist.GetPickListOrderUseCaseInterface
import javax.inject.Inject

class GetFavoriteListOrderUseCase @Inject constructor(
    private val localPickListOrderRepository: LocalPickListOrderRepository
) : GetPickListOrderUseCaseInterface {
    override suspend operator fun invoke() = localPickListOrderRepository.favoriteListOrder
}
