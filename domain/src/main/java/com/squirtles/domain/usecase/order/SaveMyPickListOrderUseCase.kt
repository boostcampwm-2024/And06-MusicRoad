package com.squirtles.domain.usecase.order

import com.squirtles.domain.model.Order
import com.squirtles.domain.repository.local.LocalPickListOrderRepository
import com.squirtles.domain.repository.local.LocalRepository
import com.squirtles.domain.usecase.picklist.SavePickListOrderUseCaseInterface
import javax.inject.Inject

class SaveMyPickListOrderUseCase @Inject constructor(
    private val localPickListOrderRepository: LocalPickListOrderRepository
) : SavePickListOrderUseCaseInterface {
    override suspend operator fun invoke(order: Order) = localPickListOrderRepository.saveMyListOrder(order)
}
