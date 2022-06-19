package com.lutalic.luboard.model

import com.lutalic.luboard.model.database.entities.Post
import com.lutalic.luboard.model.database.entities.Table
import com.lutalic.luboard.model.database.entities.User
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.BufferedReader
import java.io.FileReader

/**
 * API to interact with the database using JDBC
 */
interface DaoKt : AutoCloseable {
    var newBoardState: MutableStateFlow<Int>
    var newLocalBoardState: MutableStateFlow<Int>
    var noConnectionFlag: MutableStateFlow<Int>
    var currentUserFlow: MutableStateFlow<User>
    var newPostState : MutableStateFlow<Int>

    /**
     * Upsert new user to database
     */
    suspend fun upsertNewUser(user: User)

    /**
     * Attempt to authorize a user
     */
    suspend fun authorization(user: User)

    /**
     * Create new table with current user as admin
     */
    suspend fun createNewTable(table: Table)

    /**
     * Add table, that already exists for current user to database
     */
    suspend fun addTableToUser(table: Table, userEmail: String)

    /**
     * Upsert new post to database
     */
    suspend fun upsertPost(post: Post)

    /**
     * Update table
     */
    suspend fun updateTable(table: Table)

    /**
     * Update post
     */
    suspend fun updatePost(post: Post)

    /**
     * Remove table from database
     */
    suspend fun removeTable(table: Table)

    /**
     * Remove post from database
     */
    suspend fun removePost(post: Post)

    /**
     * Count of users in table
     */
    suspend fun usersCount(table: Table) : Int

    /**
     * @return all tables for user
     */
    suspend fun getAllTables(user: User): List<Table?>

    /**
     * @return all posts for table
     */
    suspend fun getAllPosts(table: Table): List<Post?>?

    /**
     * @return all posts for user
     */
    suspend fun getAllUserPosts(user: User): List<Post?>?

    /**
     * Read SQL-request from file
     */
    fun getSql(fileName: String): String? {
        val request = StringBuilder()
        FileReader(fileName).use { fileReader ->
            BufferedReader(fileReader).use { reader ->
                var next: String?
                while (reader.readLine().also { next = it } != null) {
                    request.append(next).append(" ")
                }
            }
        }
        return request.toString()
    }

    fun logout()

    companion object {
        val DENY_USER = User("Deny_Email", "Deny_Pass")
    }
}