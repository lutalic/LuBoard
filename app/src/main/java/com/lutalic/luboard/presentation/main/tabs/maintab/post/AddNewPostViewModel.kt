package com.lutalic.luboard.presentation.main.tabs.maintab.post

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutalic.luboard.model.DaoKt
import com.lutalic.luboard.model.database.entities.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddNewPostViewModel @Inject constructor(
    private val boardsRepository: DaoKt,
) : ViewModel() {

    fun addNewPost(
        name: String,
        description: String,
        colour: String,
        tableId: Int,
        tableName: String
    ) =
        viewModelScope.launch {

            boardsRepository.upsertPost(
                Post(
                    id = 1,
                    name = name,
                    description = description,
                    date = getCurrentDate(),
                    colour = colour,
                    tableId = tableId,
                    tableName = tableName
                )
            )
        }

    fun getCurrentDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        Log.d("DATEEE", current.format(formatter))
        return current.format(formatter)
    }
}