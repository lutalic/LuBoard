package com.lutalic.luboard.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lutalic.luboard.model.room.account.AccountEntity
import com.lutalic.luboard.model.room.post.PostEntity
import com.lutalic.luboard.model.room.post.PostsDao
import com.lutalic.luboard.model.room.tables.TableEntity
import com.lutalic.luboard.model.room.tables.TablesDao

@Database(
    entities = [
        AccountEntity::class, TableEntity::class, PostEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tablesDao(): TablesDao

    abstract fun postsDao(): PostsDao
}