package com.lutalic.luboard.screen

import com.lutalic.luboard.R
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KButton

object SignInScreen : Screen<SignInScreen>() {
    val emailEditText = KEditText {
        withId(R.id.email_sign_in)
    }
    val passwordEditText = KEditText {
        withId(R.id.password_sign_in)
    }
    val signInButton = KButton {
        withId(R.id.signInButton)
    }
}