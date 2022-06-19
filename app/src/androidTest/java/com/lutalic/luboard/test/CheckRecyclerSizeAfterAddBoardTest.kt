package com.lutalic.luboard.test

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.filters.LargeTest
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.lutalic.luboard.item.BoardItem
import com.lutalic.luboard.presentation.main.MainActivity
import com.lutalic.luboard.screen.AddNewBoardScreen
import com.lutalic.luboard.screen.BoardListScreen
import com.lutalic.luboard.setTestAccountInSharedPreferences
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.lessThan
import org.junit.Test

@LargeTest
class CheckRecyclerSizeAfterAddBoardTest : TestCase() {

    private var launchActivity: ActivityScenario<MainActivity>? = null

    @Test
    fun testAddBoard() = before {
        device.targetContext.setTestAccountInSharedPreferences()
        launchActivity = launchActivity()
    }.after {
        launchActivity?.close()
    }.run {
        var beforeSize = 0

        step("Get start list size and press add button") {
            BoardListScreen {
                boardsList {
                    flakySafely(DEFAULT_MS_TIME) { // wait async task
                        lastChild<BoardItem> {
                            isVisible()
                        }
                    }
                    beforeSize = getSize()
                }
                addButton { click() }
            }
        }

        step("Create new board") {
            AddNewBoardScreen {
                boardNameEditText {
                    isVisible()
                    click()
                    typeText(TEST_BOARD_NAME)
                    hasText(TEST_BOARD_NAME)
                }
                addButton { click() }
            }
        }

        step("Check new board size") {
            BoardListScreen {
                boardsList {
                    flakySafely(DEFAULT_MS_TIME) { // wait async task
                        lastChild<BoardItem> {
                            isVisible()
                        }
                    }
                    assertThat(beforeSize, lessThan(getSize()))
                }
            }
        }
    }

    companion object {
        const val TEST_BOARD_NAME = "New tboard"
        const val DEFAULT_MS_TIME = 7000L
    }

}