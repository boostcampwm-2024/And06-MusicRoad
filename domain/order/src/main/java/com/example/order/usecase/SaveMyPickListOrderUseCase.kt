package com.example.order.usecase

import com.example.model.Order
import com.example.order.LocalPickListOrderRepository
import com.example.picklist.SavePickListOrderUseCaseInterface
import javax.inject.Inject

class SaveMyPickListOrderUseCase @Inject constructor(
    private val localPickListOrderRepository: LocalPickListOrderRepository
) : SavePickListOrderUseCaseInterface {
    override suspend operator fun invoke(order: Order) = localPickListOrderRepository.saveMyListOrder(order)
}
