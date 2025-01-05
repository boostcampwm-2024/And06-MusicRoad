package com.squirtles.domain.usecase.order

import com.squirtles.domain.model.Order
import com.squirtles.domain.local.LocalRepository
import javax.inject.Inject

class SaveMyListOrderUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(order: Order) = localRepository.saveMyListOrder(order)
}
