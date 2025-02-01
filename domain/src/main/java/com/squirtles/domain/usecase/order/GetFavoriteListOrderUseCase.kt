package com.squirtles.domain.usecase.order

import com.squirtles.domain.local.LocalRepository
import com.squirtles.domain.usecase.picklist.GetPickListOrderUseCaseTemplate
import javax.inject.Inject

class GetFavoriteListOrderUseCase @Inject constructor(
    localRepository: LocalRepository
) : GetPickListOrderUseCaseTemplate(localRepository) {
    override suspend operator fun invoke() = localRepository.favoriteListOrder
}
