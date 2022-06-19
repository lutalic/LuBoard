package com.lutalic.luboard.utils

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import androidx.navigation.NavArgsLazy
import java.io.Serializable


fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)


inline fun <reified Args : NavArgs> SavedStateHandle.navArgs() = NavArgsLazy(Args::class) {
    val bundle = Bundle()
    keys().forEach {
        val value = get<Any>(it)
        if (value is Serializable) {
            bundle.putSerializable(it, value)
        } else if (value is Parcelable) {
            bundle.putParcelable(it, value)
        }
    }
    bundle
}