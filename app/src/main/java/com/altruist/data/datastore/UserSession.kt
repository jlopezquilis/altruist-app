package com.altruist.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.altruist.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserSession(private val context: Context) {

    companion object {
        private val ID = longPreferencesKey("id_user")
        private val NAME = stringPreferencesKey("name")
        private val EMAIL = stringPreferencesKey("email")
        private val USERNAME = stringPreferencesKey("username")
        private val SURNAME = stringPreferencesKey("surname")
        private val GENDER = stringPreferencesKey("gender")
        private val PASSWORD_HASH = stringPreferencesKey("password_hash")
        private val SITUATION = stringPreferencesKey("situation")
        private val PROFILE_PICTURE_URL = stringPreferencesKey("profile_picture_url")
        private val ANONYMOUS = booleanPreferencesKey("anonymous")
    }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { prefs ->
            prefs[ID] = user.id_user
            prefs[NAME] = user.name
            prefs[EMAIL] = user.email
            prefs[USERNAME] = user.username
            prefs[SURNAME] = user.surname ?: ""
            prefs[GENDER] = user.gender
            prefs[PASSWORD_HASH] = user.password_hash
            prefs[SITUATION] = user.situation ?: ""
            prefs[PROFILE_PICTURE_URL] = user.profile_picture_url ?: ""
            prefs[ANONYMOUS] = user.anonymous
        }
    }

    fun getUser(): Flow<User?> {
        return context.dataStore.data.map { prefs ->
            val id = prefs[ID] ?: return@map null
            val name = prefs[NAME] ?: return@map null
            val email = prefs[EMAIL] ?: return@map null
            val username = prefs[USERNAME] ?: ""
            val surname = prefs[SURNAME] ?: ""
            val gender = prefs[GENDER] ?: ""
            val password = prefs[PASSWORD_HASH] ?: ""
            val situation = prefs[SITUATION] ?: ""
            val profilePic = prefs[PROFILE_PICTURE_URL] ?: ""
            val anonymous = prefs[ANONYMOUS] ?: false

            User(
                id_user = id,
                name = name,
                surname = surname,
                username = username,
                gender = gender,
                email = email,
                password_hash = password,
                situation = situation,
                profile_picture_url = profilePic,
                anonymous = anonymous
            )
        }
    }

    suspend fun updateUser(updatedUser: User) {
        saveUser(updatedUser)
    }


    suspend fun clearUser() {
        context.dataStore.edit { it.clear() }
    }
}
