package com.d204.rumeet.data.local.sharedpreference

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.d204.rumeet.data.R
import kotlinx.coroutines.flow.first

class UserDataStorePreferences(val context: Context) {
    private val Context.datastore by preferencesDataStore(name = context.getString(R.string.prefs_key))

    private val firstRun = booleanPreferencesKey("FIRST_RUN")
    private val autoLogin = booleanPreferencesKey("AUTO_LOGIN")
    private val accessToken = stringPreferencesKey("ACCESS_TOKEN")
    private val refreshToken = stringPreferencesKey("REFRESH_TOKEN")
    private val fcmToken = stringPreferencesKey("FCM_TOKEN")

    // dataStore는 비동기 기반
    suspend fun setFirstRun(firstRunState: Boolean) {
        context.datastore.edit { preference ->
            preference[firstRun] = firstRunState
        }
    }

    suspend fun getFirstRun() : Boolean{
        return context.datastore.data.first().let {
            it[firstRun] ?: false
        }
    }

    suspend fun setAutoLogin(autoState: Boolean) {
        context.datastore.edit { preference ->
            preference[autoLogin] = autoState
        }
    }

    suspend fun getAutoLogin() : Boolean{
        return context.datastore.data.first().let {
            it[autoLogin] ?: false
        }
    }
}