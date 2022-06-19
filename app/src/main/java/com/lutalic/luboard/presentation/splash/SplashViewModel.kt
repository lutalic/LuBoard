package com.lutalic.luboard.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lutalic.luboard.model.DaoKt
import com.lutalic.luboard.utils.MutableLiveEvent
import com.lutalic.luboard.utils.publishEvent
import com.lutalic.luboard.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * SplashViewModel checks whether user is signed-in or not.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    val dao: DaoKt
) : ViewModel() {

    private val _launchMainScreenEvent = MutableLiveEvent<Boolean>()
    val launchMainScreenEvent = _launchMainScreenEvent.share()

    init {
        viewModelScope.launch {
            delay(1000)
            _launchMainScreenEvent.publishEvent(true)
        }
    }
}