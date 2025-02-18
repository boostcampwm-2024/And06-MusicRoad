package com.example.order

import com.example.model.Order
import javax.inject.Singleton

@Singleton
class LocalPickListOrderRepositoryImpl : LocalPickListOrderRepository {
    private var _favoriteListOrder = Order.LATEST
    override val favoriteListOrder get() = _favoriteListOrder

    private var _myListOrder = Order.LATEST
    override val myListOrder get() = _myListOrder

    override suspend fun saveFavoriteListOrder(order: Order) {
        _favoriteListOrder = order
    }

    override suspend fun saveMyListOrder(order: Order) {
        _myListOrder = order
    }
}
