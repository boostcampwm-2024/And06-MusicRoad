package com.squirtles.domain.usecase.picklist

import com.squirtles.domain.local.LocalRepository
import com.squirtles.domain.model.Order

abstract class SavePickListOrderUseCaseTemplate(
    val localRepository: LocalRepository
) {
    abstract suspend operator fun invoke(order: Order)
}
