package com.squirtles.domain.usecase.picklist

import com.squirtles.domain.model.Order

interface SavePickListOrderUseCaseInterface {
    suspend operator fun invoke(order: Order)
}
