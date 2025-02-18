package com.example.picklist

import com.example.model.Order

interface SavePickListOrderUseCaseInterface {
    suspend operator fun invoke(order: Order)
}
