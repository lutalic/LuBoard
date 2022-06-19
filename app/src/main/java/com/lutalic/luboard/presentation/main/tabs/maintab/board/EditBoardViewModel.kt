package com.lutalic.luboard.presentation.main.tabs.maintab.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutalic.luboard.model.DaoKt
import com.lutalic.luboard.model.database.entities.Table
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditBoardViewModel @Inject constructor(
    private val dao: DaoKt
) : ViewModel() {
    fun editTable(table: Table) =viewModelScope.launch{
        dao.updateTable(table)
    }
}