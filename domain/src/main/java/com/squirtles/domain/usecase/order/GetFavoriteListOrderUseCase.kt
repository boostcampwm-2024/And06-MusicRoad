package com.squirtles.domain.usecase.order

import com.squirtles.domain.repository.local.LocalRepository
import com.squirtles.domain.usecase.picklist.GetPickListOrderUseCaseInterface
import javax.inject.Inject

class GetFavoriteListOrderUseCase @Inject constructor(
    private val localRepository: LocalRepository
) : GetPickListOrderUseCaseInterface {
    override suspend operator fun invoke() = localRepository.favoriteListOrder
}
