package com.squirtles.domain.usecase.order

import com.squirtles.domain.repository.local.LocalPickListOrderRepository
import com.squirtles.domain.repository.local.LocalRepository
import com.squirtles.domain.usecase.picklist.GetPickListOrderUseCaseInterface
import javax.inject.Inject

class GetFavoriteListOrderUseCase @Inject constructor(
    private val localPickListOrderRepository: LocalPickListOrderRepository
) : GetPickListOrderUseCaseInterface {
    override suspend operator fun invoke() = localPickListOrderRepository.favoriteListOrder
}
