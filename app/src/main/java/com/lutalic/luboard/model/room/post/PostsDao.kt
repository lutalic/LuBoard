package com.lutalic.luboard.model.room.post

import androidx.room.*
import com.lutalic.luboard.model.database.entities.Post
import com.lutalic.luboard.model.database.entities.Table
import com.lutalic.luboard.model.room.post.PostEntity
import com.lutalic.luboard.model.room.tables.TableEntity

@Dao
interface PostsDao {

    @Insert(entity = PostEntity::class)
    suspend fun createPost(postEntity: PostEntity)

    @Delete(entity = PostEntity::class)
    suspend fun deletePost(postEntity: PostEntity)

    @Query("SELECT * FROM posts ORDER by id")
    suspend fun getAll(): List<Post>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg postEntity: PostEntity)

    @Query("DELETE FROM posts")
    suspend fun clearAll()

    @Query("SELECT * FROM posts WHERE tableId = :tableId")
    fun getAllForTable(tableId: Int): List<Post>
}
