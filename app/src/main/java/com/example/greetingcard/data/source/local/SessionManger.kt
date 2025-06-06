package com.example.greetingcard.data.source.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.greetingcard.data.model.response.LoginResponse
import com.example.greetingcard.data.model.response.UserInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Context의 확장 프로퍼티로 DataStore 정의 (권장 방식)
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val KEY_ACCESS_TOKEN = stringPreferencesKey("access_token")
        val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")

        val KEY_USER_ID = intPreferencesKey("user_id")
        val KEY_USER_NAME = stringPreferencesKey("user_name")
        val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        val KEY_PROFILE_PIC = stringPreferencesKey("profile_pic")
    }

    suspend fun saveLoginSession(response: LoginResponse) {
        context.dataStore.edit { prefs ->
            // 토큰 저장
            prefs[KEY_ACCESS_TOKEN] = response.accessToken
            prefs[KEY_REFRESH_TOKEN] = response.refreshToken
            // UserInfo 저장
            prefs[KEY_USER_ID] = response.user.id
            prefs[KEY_USER_NAME] = response.user.name
            prefs[KEY_USER_EMAIL] = response.user.email
            prefs[KEY_PROFILE_PIC] = response.user.profilePic
        }
    }

    val userInfoFlow: Flow<UserInfo> = context.dataStore.data.map { prefs ->
        UserInfo(
            id = prefs[KEY_USER_ID] ?: 0,
            email = prefs[KEY_USER_EMAIL] ?: "",
            name = prefs[KEY_USER_NAME] ?: "",
            oauthId = "",  // 선택
            profilePic = prefs[KEY_PROFILE_PIC] ?: ""
        )
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        val token = prefs[KEY_ACCESS_TOKEN]
        !token.isNullOrEmpty()
    }

    val accessTokenFlow: Flow<String?> = context.dataStore.data
        .map { prefs -> prefs[KEY_ACCESS_TOKEN] }


}
