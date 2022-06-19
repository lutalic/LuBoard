package com.lutalic.luboard.di

import android.content.Context
import com.lutalic.luboard.presentation.uiactions.SimpleUiActions
import com.lutalic.luboard.presentation.uiactions.UiActions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    @Singleton
    fun provideUiAction(@ApplicationContext context: Context): UiActions {
        return SimpleUiActions(context)
    }

}