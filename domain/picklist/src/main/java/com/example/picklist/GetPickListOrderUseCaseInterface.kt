package com.example.picklist

import com.example.model.Order

interface GetPickListOrderUseCaseInterface {
    suspend operator fun invoke(): Order
}
