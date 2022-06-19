package com.lutalic.luboard

import android.content.Context
import android.content.SharedPreferences
import com.lutalic.luboard.presentation.main.MainActivity


fun Context.signOutUserInSharedPreferences() {
    val preferences: SharedPreferences =
        this.getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE)
    preferences.edit()
        .putString(MainActivity.ACCOUNT_EMAIL, "Deny_Email")
        .putString(MainActivity.ACCOUNT_PASS, "Deny_Pass")
        .apply()
}

fun Context.setTestAccountInSharedPreferences() {
    val preferences: SharedPreferences =
        this.getSharedPreferences(MainActivity.TAG, Context.MODE_PRIVATE)
    preferences.edit()
        .putString(MainActivity.ACCOUNT_EMAIL, "gl.k@aol.com")
        .putString(MainActivity.ACCOUNT_PASS, "qwerty001")
        .apply()
}