package com.lutalic.luboard.screen

import com.lutalic.luboard.R
import com.lutalic.luboard.item.BoardItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KButton

object BoardListScreen : Screen<BoardListScreen>() {
    val addButton: KButton = KButton {
        withId(R.id.floating_button_add_board)
    }

    val boardsList: KRecyclerView = KRecyclerView(builder = {
        withId(R.id.boards_recycler_view)
    }, itemTypeBuilder = {
        itemType(::BoardItem)
    })
}