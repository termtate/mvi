package com.example.myapplicationg.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account")

class AccountDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val TOKEN = stringPreferencesKey("token")
    }

    val tokenFlow: Flow<String> = context.dataStore.data
        .map {
            it[TOKEN] ?: ""
        }

    suspend fun getToken() = tokenFlow.first()


    suspend fun setToken(token: String) = context.dataStore.edit {
        it[TOKEN] = token
    }
}