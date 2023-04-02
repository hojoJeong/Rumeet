package com.d204.rumeet.data.local.datastore

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.d204.rumeet.data.R
import kakao.k.p
import kotlinx.coroutines.flow.*
import java.io.IOException

internal class UserDataStorePreferences(val context: Context) {
    private val Context.datastore by preferencesDataStore(name = context.getString(R.string.prefs_key))

    private val firstAccess = booleanPreferencesKey("FIRST_ACCESS")
    private val autoLogin = booleanPreferencesKey("AUTO_LOGIN")
    private val accessToken = stringPreferencesKey("ACCESS_TOKEN")
    private val refreshToken = stringPreferencesKey("REFRESH_TOKEN")
    private val userId = intPreferencesKey("USER_ID")
    private val fcmToken = stringPreferencesKey("FCM_TOKEN")

    suspend fun setUserId(userId : Int){
        Log.d(TAG, "setUserId: $userId")
        context.datastore.edit { preference ->
            preference[this.userId] = userId
        }
    }

    suspend fun getUserId() : Int{
        return context.datastore.data.first().let {
            it[userId] ?: -1
        }
    }

    // dataStore는 비동기 기반
    suspend fun setFirstRun(firstRunState: Boolean) {
        context.datastore.edit { preference ->
            preference[firstAccess] = firstRunState
        }
    }

    suspend fun getFirstRun() : Boolean{
        return context.datastore.data.first().let {
            it[firstAccess] ?: false
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

    suspend fun setToken(newAccessToken : String, newRefreshToken : String){
        context.datastore.edit { preference ->
            preference[accessToken] = newAccessToken
        }

        context.datastore.edit { preference ->
            preference[refreshToken] = newRefreshToken
        }
    }

    suspend fun getAccessToken() : String?{
        return context.datastore.data.first().let {
            it[accessToken]
        }
    }

    suspend fun getRefreshToken() : String?{
        return context.datastore.data.first().let {
            it[refreshToken]
        }
    }

    suspend fun clearUserInfo(){
        context.datastore.edit { preference ->
            preference[userId] = -1
            preference[accessToken] = ""
            preference[refreshToken] = ""
            preference[autoLogin] = false
        }
    }
}