package com.lutalic.luboard.app

import android.app.Application
import androidx.room.Room
import com.lutalic.luboard.model.room.AppDatabase
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class LuApp : Application()