package com.squirtles.data.datasource.local

import android.content.Context
import android.location.Location
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.squirtles.domain.datasource.LocalDataSource
import com.squirtles.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val context: Context,
) : LocalDataSource {
    private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)

    private lateinit var _currentUser: User
    override val currentUser: User
        get() = _currentUser

    private var _currentLocation: MutableStateFlow<Location?> = MutableStateFlow(null)
    override val lastLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    override fun readUserId(): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(USER_ID_KEY)
        return context.dataStore.data.map { preferences ->
            preferences[dataStoreKey]
        }
    }

    override suspend fun saveUserId(userId: String) {
        val dataStoreKey = stringPreferencesKey(USER_ID_KEY)
        context.dataStore.edit { preferences ->
            preferences[dataStoreKey] = userId
        }
    }

    override suspend fun saveCurrentUser(user: User) {
        _currentUser = user
    }

    override suspend fun saveCurrentLocation(location: Location) {
        _currentLocation.emit(location)
    }

    companion object {
        private const val USER_PREFERENCES_NAME = "user_preferences"
        private const val USER_ID_KEY = "user_id"
    }
}
