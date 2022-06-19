package com.lutalic.luboard.presentation.main.tabs.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutalic.luboard.model.DaoKt
import com.lutalic.luboard.model.database.entities.User
import com.lutalic.luboard.model.room.LuBoardRoomRepository
import com.lutalic.luboard.utils.MutableLiveEvent
import com.lutalic.luboard.utils.publishEvent
import com.lutalic.luboard.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dao: DaoKt,
    private val roomRepository: LuBoardRoomRepository
) : ViewModel() {

    private val _account = MutableLiveData<User>()
    val account = _account.share()

    private val _restartFromLoginEvent = MutableLiveEvent<Unit>()
    val restartWithSignInEvent = _restartFromLoginEvent.share()

    init {
        viewModelScope.launch {
            dao.currentUserFlow.collect {
                _account.value = it
            }
        }
    }

    fun logout() {
        dao.logout()
        restartAppFromLoginScreen()
        viewModelScope.launch {
            roomRepository.clearPosts()
            roomRepository.clearTables()
        }
    }

    private fun restartAppFromLoginScreen() {
        _restartFromLoginEvent.publishEvent()
    }

}