package com.lutalic.luboard.presentation.main.tabs.maintab.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutalic.luboard.model.DaoKt
import com.lutalic.luboard.model.database.entities.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPostViewModel @Inject constructor(
    private val dao: DaoKt
) : ViewModel() {
    fun editPost(post: Post) = viewModelScope.launch {
        dao.updatePost(post)
    }
}