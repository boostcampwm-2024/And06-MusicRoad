package com.example.navigation

import com.example.model.Song
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
sealed interface SearchRoute : Route {
    @Serializable
    data class Create(val song: Song) : SearchRoute
}
