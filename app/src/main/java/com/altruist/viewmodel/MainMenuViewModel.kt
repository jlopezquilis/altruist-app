package com.altruist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altruist.data.datastore.UserSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainMenuViewModel @Inject constructor(
    private val userSession: UserSession
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            userSession.clearUser()
        }
    }
}
