package com.squirtles.domain.usecase.picklist

import com.squirtles.domain.model.Order

interface GetPickListOrderUseCaseInterface {
    suspend operator fun invoke(): Order
}
