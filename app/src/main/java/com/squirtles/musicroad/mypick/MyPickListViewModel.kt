package com.squirtles.musicroad.mypick

import com.squirtles.domain.usecase.mypick.DeletePickUseCase
import com.squirtles.domain.usecase.mypick.FetchMyPicksUseCase
import com.squirtles.domain.usecase.order.GetMyPickListOrderUseCase
import com.squirtles.domain.usecase.order.SaveMyPickListOrderUseCase
import com.squirtles.musicroad.common.picklist.PickListViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyPickListViewModel @Inject constructor(
    fetchMyPicksUseCase: FetchMyPicksUseCase,
    getMyPickListOrderUseCase: GetMyPickListOrderUseCase,
    saveMyPickListOrderUseCase: SaveMyPickListOrderUseCase,
    deletePickUseCase: DeletePickUseCase,
) : PickListViewModel(
    fetchMyPicksUseCase,
    getMyPickListOrderUseCase,
    saveMyPickListOrderUseCase,
    deletePickUseCase
)
