package com.lutalic.luboard.presentation.main.tabs.maintab.boardslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutalic.luboard.model.DaoKt
import com.lutalic.luboard.model.database.entities.Table
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewBoardViewModel @Inject constructor(
    private val boardsRepository: DaoKt
) : ViewModel() {

    fun addNewBoard(id: Int, name: String) {

        viewModelScope.launch {
            boardsRepository.createNewTable(
                Table(id, name, boardsRepository.currentUserFlow.value.email)
            )
        }
    }
}