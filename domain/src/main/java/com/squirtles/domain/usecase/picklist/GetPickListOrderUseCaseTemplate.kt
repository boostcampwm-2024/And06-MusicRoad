package com.squirtles.domain.usecase.picklist

import com.squirtles.domain.local.LocalRepository
import com.squirtles.domain.model.Order

abstract class GetPickListOrderUseCaseTemplate(
    val localRepository: LocalRepository
) {
    abstract suspend operator fun invoke(): Order
}
