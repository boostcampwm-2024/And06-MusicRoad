package com.squirtles.domain.usecase.order

import com.squirtles.domain.local.LocalRepository
import com.squirtles.domain.model.Order
import com.squirtles.domain.usecase.picklist.SavePickListOrderUseCaseInterface
import javax.inject.Inject

class SaveMyPickListOrderUseCase @Inject constructor(
    private val localRepository: LocalRepository
) : SavePickListOrderUseCaseInterface {
    override suspend operator fun invoke(order: Order) = localRepository.saveMyListOrder(order)
}
