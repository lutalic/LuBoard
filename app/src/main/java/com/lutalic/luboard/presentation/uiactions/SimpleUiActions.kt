package com.lutalic.luboard.presentation.uiactions

import android.content.Context
import android.widget.Toast

/**
 * Android implementation of [UiActions]. Displaying simple toast message and getting string from resources.
 */
class SimpleUiActions(
    private val appContext: Context
) : UiActions {

    override fun showMessageForUser(message: String) {
        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
    }

    override fun getString(messageRes: Int, vararg args: Any): String {
        return appContext.getString(messageRes, *args)
    }

}