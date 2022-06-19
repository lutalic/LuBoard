package com.lutalic.luboard.presentation.main.tabs.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutalic.luboard.model.DaoKt
import com.lutalic.luboard.model.database.entities.Post
import com.lutalic.luboard.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private var dao: DaoKt
) : ViewModel() {
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
        _posts.value = dao.getAllUserPosts(dao.currentUserFlow.value)
    }

    fun removePost(post: Post) = viewModelScope.launch {
        dao.removePost(post)
    }
}