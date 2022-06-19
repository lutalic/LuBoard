package com.lutalic.luboard.item

import android.view.View
import com.lutalic.luboard.R
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

class BoardItem(matcher: Matcher<View>) : KRecyclerItem<BoardItem>(matcher) {
    val name: KTextView = KTextView {
        withId(R.id.title_board)
    }
    val userCount: KTextView = KTextView {
        withId(R.id.user_count_for_board)
    }
}