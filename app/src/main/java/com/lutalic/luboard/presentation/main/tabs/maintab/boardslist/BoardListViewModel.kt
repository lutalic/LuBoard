package com.lutalic.luboard.presentation.main.tabs.maintab.boardslist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutalic.luboard.model.DaoKt
import com.lutalic.luboard.model.database.entities.Table
import com.lutalic.luboard.model.room.LuBoardRoomRepository
import com.lutalic.luboard.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import javax.inject.Inject

@HiltViewModel
class BoardListViewModel @Inject constructor(
    private val dao: DaoKt,
    private val roomDao: LuBoardRoomRepository,
) : ViewModel() {
    private val _boards = MutableLiveData<List<Table?>>()
    val boards = _boards.share()

    init {
        viewModelScope.launch {
            dao.newLocalBoardState.collect {
                localInitMethod()
            }
        }
        viewModelScope.launch {
            dao.newBoardState.collect {
                initMethod()
            }
        }

    }

    fun initMethod() {
        viewModelScope.launch {
            dao.currentUserFlow.value.let {
                _boards.value = dao.getAllTables(it)
            }
        }
    }

    fun localInitMethod() {
        viewModelScope.launch {
            dao.currentUserFlow.value.let {
                _boards.value = roomDao.getAllTables()
            }
        }
    }

    fun removeBoard(table: Table) {
        viewModelScope.launch {
            dao.removeTable(table)
        }
    }

    fun addTableToUser(table: Table, email:String) {
        viewModelScope.launch {
            dao.addTableToUser(table, email)
        }
    }
}