package com.lutalic.luboard.test

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.filters.LargeTest
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.lutalic.luboard.presentation.main.MainActivity
import com.lutalic.luboard.screen.BoardListScreen
import com.lutalic.luboard.screen.SignInScreen
import com.lutalic.luboard.signOutUserInSharedPreferences
import org.junit.Test

@LargeTest
class SignInTest : TestCase() {
    private var launchActivity: ActivityScenario<MainActivity>? = null

    @Test
    fun testLogIn() = before {
        device.targetContext.signOutUserInSharedPreferences()
        launchActivity = launchActivity()
    }.after {
        launchActivity?.close()
    }.run {
        step("Input email and password") {
            SignInScreen {
                emailEditText {
                    click()
                    typeText(TEST_USER_EMAIL)
                    hasText(TEST_USER_EMAIL)
                }

                passwordEditText {
                    click()
                    typeText(TEST_USER_PASSWORD)
                    hasText(TEST_USER_PASSWORD)
                }
            }
        }

        step("Go to board screen") {
            SignInScreen {
                signInButton {
                    click()
                }
            }
        }

        step("Check BoardList screen is visible") {
            BoardListScreen.addButton.isVisible()
        }
    }

    companion object {
        const val TEST_USER_EMAIL = "gl.k@aol.com"
        const val TEST_USER_PASSWORD = "qwerty001"
    }
}