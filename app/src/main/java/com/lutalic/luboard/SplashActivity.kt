package com.lutalic.luboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.lutalic.luboard.presentation.splash.SplashFragment
import com.lutalic.luboard.presentation.splash.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Entry point of the app.
 *
 * Splash activity contains only window background, all other initialization logic is placed to
 * [SplashFragment] and [SplashViewModel].
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MDAA", "on create splash")

        setContentView(R.layout.activity_splash)
        Log.d("MDAA", "on setContentView splash")

        //Repositories.dao.currentUserFlow.value = User("gl.k@aol.com", "qwerty001")
    }
}