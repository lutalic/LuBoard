package com.lutalic.luboard.presentation.main.tabs.maintab.board

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutalic.luboard.model.DaoKt
import com.lutalic.luboard.model.database.entities.Post
import com.lutalic.luboard.model.database.entities.Table
import com.lutalic.luboard.utils.navArgs
import com.lutalic.luboard.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val dao: DaoKt,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val board = savedStateHandle.navArgs<BoardFragmentArgs>().value.board
    private val _posts = MutableLiveData<List<Post?>>()
    val posts = _posts.share()


    init {
        viewModelScope.launch {
            dao.newPostState.collect {
                init()
            }
        }
    }

    fun init() = viewModelScope.launch {
        _posts.value =
            dao.getAllPosts(Table(board.id, board.name, dao.currentUserFlow.value.email))
    }

    fun removePost(post: Post) = viewModelScope.launch {
        dao.removePost(post)
    }
}
