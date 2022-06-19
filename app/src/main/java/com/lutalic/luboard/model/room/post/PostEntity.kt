package com.lutalic.luboard.model.room.post

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lutalic.luboard.model.database.entities.Post

@Entity(
    tableName = "posts"
)
data class PostEntity(
    @PrimaryKey var id: Int,
    var name: String,
    var description: String,
    val date: String,
    var colour: String,
    val tableId: Int,
    val tableName: String
){

    companion object {
        fun postToPostEntity(post: Post) = PostEntity(
            post.id,
            post.name,
            post.description,
            post.date,
            post.colour,
            post.tableId,
            post.tableName
        )

        fun postEntityToPost(postEntity: PostEntity) = Post(
            postEntity.id,
            postEntity.name,
            postEntity.description,
            postEntity.date,
            postEntity.colour,
            postEntity.tableId,
            postEntity.tableName
        )
    }


}
