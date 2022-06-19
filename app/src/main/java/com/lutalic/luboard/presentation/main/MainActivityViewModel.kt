package com.lutalic.luboard.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutalic.luboard.model.DaoKt
import com.lutalic.luboard.model.database.entities.User
import com.lutalic.luboard.model.room.LuBoardRoomRepository
import com.lutalic.luboard.model.room.tables.TableEntity
import com.lutalic.luboard.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dao: DaoKt,
    private val roomRepository: LuBoardRoomRepository
) : ViewModel() {
    private val _noConnection = MutableLiveData<Int>(0)
    val noConnection = _noConnection.share()

    init {
        viewModelScope.launch {
            dao.noConnectionFlag.collect {
                _noConnection.value = _noConnection.value ?: 0 + 1
            }
        }
    }

    fun setUser(email: String, password: String) {
        dao.currentUserFlow.value = User(email, password)
    }

    fun getCurrentEmail() = dao.currentUserFlow.value.email

    fun getCurrentPassword() = dao.currentUserFlow.value.password

    fun isSignedIn(): Boolean {
        return dao.currentUserFlow.value != DaoKt.DENY_USER
    }

}