package com.squirtles.domain.picklist

import com.squirtles.domain.model.Order

interface SavePickListOrderUseCaseInterface {
    suspend operator fun invoke(order: Order)
}
