package com.lutalic.luboard.screen

import com.lutalic.luboard.R
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KButton

object AddNewBoardScreen : Screen<AddNewBoardScreen>() {
    val boardNameEditText = KEditText {
        withId(R.id.name_of_board_input)
    }
    val addButton = KButton {
        withId(R.id.create_new_board_btn)
    }
}