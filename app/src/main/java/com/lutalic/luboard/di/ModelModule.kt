package com.lutalic.luboard.di

import android.content.Context
import androidx.room.Room
import com.lutalic.luboard.model.DaoKt
import com.lutalic.luboard.model.database.LuDaoKt
import com.lutalic.luboard.model.room.AppDatabase
import com.lutalic.luboard.model.room.post.PostsDao
import com.lutalic.luboard.model.room.LuBoardRoomRepository
import com.lutalic.luboard.model.room.tables.TablesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ModelModule {

    @Provides
    @Singleton
    fun provideDao(luBoardRoomRepository: LuBoardRoomRepository): DaoKt =
        LuDaoKt(luBoardRoomRepository)

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "databbase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRoomRepository(postsDao: PostsDao, tablesDao: TablesDao): LuBoardRoomRepository {
        return LuBoardRoomRepository(postsDao, tablesDao)
    }

    @Provides
    @Singleton
    fun providePostsDao(appDatabase: AppDatabase): PostsDao {
        return appDatabase.postsDao()
    }


    @Provides
    @Singleton
    fun provideTablesDao(appDatabase: AppDatabase): TablesDao {
        return appDatabase.tablesDao()
    }

}