package com.lutalic.luboard.model.room

import com.lutalic.luboard.model.database.entities.Post
import com.lutalic.luboard.model.database.entities.Table
import com.lutalic.luboard.model.room.post.PostEntity.Companion.postEntityToPost
import com.lutalic.luboard.model.room.post.PostEntity.Companion.postToPostEntity
import com.lutalic.luboard.model.room.post.PostsDao
import com.lutalic.luboard.model.room.tables.TableEntity
import com.lutalic.luboard.model.room.tables.TableEntity.Companion.tableToTableEntity
import com.lutalic.luboard.model.room.tables.TablesDao

class LuBoardRoomRepository(
    private val postsDao: PostsDao,
    private val tablesDao: TablesDao
) {
    suspend fun createTable(vararg table: Table) {
        tablesDao.insertAll(*table.map { tableToTableEntity(it) }.toTypedArray())
    }

    suspend fun deleteTable(table: Table) {
        tablesDao.deleteTable(tableToTableEntity(table))
    }

    suspend fun clearTables() {
        tablesDao.clearTables()
    }


    suspend fun getAllTables(): List<Table> {
        return tablesDao.getAll().map { it.tableEntityToTable() }
    }

    suspend fun getAllPosts():List<Post>{
        return postsDao.getAll()
    }

    suspend fun createPost(vararg post: Post) {
        postsDao.insertAll(*post.map { postToPostEntity(it) }.toTypedArray())
    }

    suspend fun clearPosts(){
        postsDao.clearAll()
    }

    suspend fun deletePost(post: Post) {
        postsDao.deletePost(postToPostEntity(post))
    }

    suspend fun getAllPostForTable(table: Table): List<Post> {
        return postsDao.getAllForTable(table.id)
    }
}