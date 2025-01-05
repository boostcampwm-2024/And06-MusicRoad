package com.squirtles.domain.usecase.order

import com.squirtles.domain.local.LocalRepository
import javax.inject.Inject

class GetMyPickListOrderUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    operator fun invoke() = localRepository.myListOrder
}
