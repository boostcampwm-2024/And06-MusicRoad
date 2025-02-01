package com.squirtles.domain.usecase.order

import com.squirtles.domain.local.LocalRepository
import com.squirtles.domain.model.Order
import com.squirtles.domain.usecase.picklist.SavePickListOrderUseCaseTemplate
import javax.inject.Inject

class SaveFavoriteListOrderUseCase @Inject constructor(
    localRepository: LocalRepository
) : SavePickListOrderUseCaseTemplate(localRepository) {
    override suspend operator fun invoke(order: Order) = localRepository.saveFavoriteListOrder(order)
}
