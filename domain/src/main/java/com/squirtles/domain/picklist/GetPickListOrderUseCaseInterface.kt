package com.squirtles.domain.picklist

import com.squirtles.domain.model.Order

interface GetPickListOrderUseCaseInterface {
    suspend operator fun invoke(): Order
}
