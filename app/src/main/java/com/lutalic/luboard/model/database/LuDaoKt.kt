package com.lutalic.luboard.model.database

import android.util.Log
import com.lutalic.luboard.model.*
import com.lutalic.luboard.model.DaoKt.Companion.DENY_USER
import com.lutalic.luboard.model.database.entities.Post
import com.lutalic.luboard.model.database.entities.Table
import com.lutalic.luboard.model.database.entities.User
import com.lutalic.luboard.model.room.LuBoardRoomRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.security.spec.ECField
import java.sql.*
import java.util.*
import javax.inject.Inject

class LuDaoKt @Inject constructor(
    var roomRepository: LuBoardRoomRepository
) : DaoKt {
    private lateinit var connection: Connection
    private lateinit var properties: Properties
    private var lastTables: MutableList<Table>? = null
    override var currentUserFlow = MutableStateFlow(DENY_USER)
    override var newPostState = MutableStateFlow(0)
    override var newBoardState = MutableStateFlow(0)
    override var newLocalBoardState = MutableStateFlow(0)
    override var noConnectionFlag = MutableStateFlow(0)


    override suspend fun upsertNewUser(user: User) = withContext(Dispatchers.IO) {
        if (!::connection.isInitialized)
            createDao()
        if (user.email.isBlank()) {
            throw EmptyFieldException(Field.Email)
        }
        if (user.password.isBlank()) {
            throw EmptyFieldException(Field.Password)
        }

        // TODO implement hashPassword()
        val sql = "insert into users (email, password)\n" +
                "values (?, ?)\n"
        val runCatchingResult = runCatching {
            connection.prepareStatement(sql).use { statement ->
                // Set users params
                statement.setString(1, user.email)
                statement.setString(2, user.password)
                statement.executeUpdate()
            }
        }
        if (runCatchingResult.isFailure) {
            throw AuthException("Registration failure")
        }
        currentUserFlow.value = user
    }


    override suspend fun authorization(user: User) = withContext(Dispatchers.IO) {
        if (!::connection.isInitialized)
            createDao()
        val (_, password) = isUserExists(user.email)
            ?: throw NoSuchUserException("No such user")
        if (password != user.password) {
            throw PasswordMismatchException("Password mismatch")
        }
        currentUserFlow.value = user

    }


    override suspend fun createNewTable(table: Table): Unit = withContext(Dispatchers.IO) {
        roomRepository.createTable(table)
        try {

            var sql = "insert into tables (id, name, admin_email)\n" +
                    "values (?, ?, ?)\n"
            val result: MutableList<Table> = ArrayList()
            val sql1 = "select max(id)\n" +
                    "from tables"
            var max = 1
            connection.prepareStatement(sql1).use { statement ->
                // Set user's email for SQL-request
                statement.executeQuery().use { set ->
                    // Get all found tables
                    while (set.next()) {
                        max = set.getInt("max") + 1
                    }
                }
            }
            table.id = max
            connection.prepareStatement(sql).use { statement ->
                // Set table params
                statement.setInt(1, table.id)
                statement.setString(2, table.name)
                statement.setString(3, table.admin)
                statement.executeUpdate()
            }
            sql = "insert into user_table(user_email, table_id)\n" +
                    "values(?, ?)\n"
            connection.prepareStatement(sql).use { statement ->
                // set user_table params
                statement.setString(1, currentUserFlow.value.email)
                statement.setInt(2, table.id)
                statement.executeUpdate()
            }

        } catch (e: Exception) {

        } finally {
            newBoardState.value++
        }
    }


    override suspend fun addTableToUser(table: Table, userEmail: String): Unit =
        withContext(Dispatchers.IO) {
            val sql = "insert into user_table(user_email, table_id)\n" +
                    "values(?, ?)\n"
            connection.prepareStatement(sql).use { statement ->
                // set user_table params
                statement.setString(1, userEmail)
                statement.setInt(2, table.id)
                statement.executeUpdate()
            }
        }

    override suspend fun upsertPost(post: Post): Unit = withContext(Dispatchers.IO) {
        val sql = "insert into posts(id, name, description, date, colour, table_id)\n" +
                "values (?, ?, ?, ?::date, ?, ?)\n"
        Log.d("DATEEEe", post.date)
        var statement: PreparedStatement? = null
        val result: MutableList<Post> = ArrayList()
        val sql1 = "select max(id)\n" +
                "from posts"
        var max: Int = 1
        var set: ResultSet? = null
        try {
            statement = connection.prepareStatement(sql1)
            // Set table's id for SQL-request
            set = statement.executeQuery()
            // Get all found posts
            while (set.next()) {
                max = set.getInt("max") + 1
            }
        } catch (e: Exception) {
            Log.e("ALO getAllUserPosts", e.message ?: e.toString())
        } finally {
            statement?.close()
            set?.close()
        }
        post.id = max
        try {
            statement = connection.prepareStatement(sql)
            // set post's params
            statement.setInt(1, post.id)
            statement.setString(2, post.name)
            statement.setString(3, post.description)
            statement.setString(4, post.date)
            statement.setString(5, post.colour)
            statement.setInt(6, post.tableId)
            statement.executeUpdate()
        } catch (e: Exception) {
            Log.e("ALO mda", e.message ?: e.toString())
        } finally {
            newPostState.value++
            statement?.close()
        }
        newPostState.value++
    }

    override suspend fun updateTable(table: Table): Unit = withContext(Dispatchers.IO) {
        try {
            val sql = "update tables\n" +
                    "set name = ?, admin_email = ?\n" +
                    "where id = ?\n"
            connection.prepareStatement(sql).use { statement ->
                // set table params
                statement.setString(1, table.name)
                statement.setString(2, table.admin)
                statement.setInt(3, table.id)
                statement.executeUpdate()
            }
            newBoardState.value++
        } catch (e: Exception) {

        }
    }

    override suspend fun updatePost(post: Post): Unit = withContext(Dispatchers.IO) {
        try {
            val sql = "update posts\n" +
                    "set name = ?, description = ?, date = ?::date, colour = ?\n" +
                    "where id = ?\n"
            connection.prepareStatement(sql).use { statement ->
                // set post's params
                statement.setString(1, post.name)
                statement.setString(2, post.description)
                statement.setString(3, post.date)
                statement.setString(4, post.colour)
                statement.setInt(5, post.id)
                statement.executeUpdate()
            }
            newPostState.value++
        } catch (e: Exception) {

        }
    }

    override suspend fun removeTable(table: Table): Unit = withContext(Dispatchers.IO) {
        try {

            val sql = "delete from tables\n" +
                    "where id = ?\n"
            connection.prepareStatement(sql).use { statement ->
                // Set table's id for SQL-request
                statement.setInt(1, table.id)
                statement.executeUpdate()
            }
            newBoardState.value++
        } catch (e: Exception) {

        }
    }

    override suspend fun removePost(post: Post): Unit = withContext(Dispatchers.IO) {
        try {
            val sql = "delete from posts\n" +
                    "where id = ?\n"
            connection.prepareStatement(sql).use { statement ->
                // Set post's id for SQL-request
                statement.setInt(1, post.id)
                statement.executeUpdate()
            }
            newPostState.value++
        } catch (e: Exception) {

        }
    }

    override fun close() {
        connection.close()
    }

    override suspend fun usersCount(table: Table): Int = withContext(Dispatchers.IO) {
        val sql = "select count(user_email)\n" +
                "from user_table\n" +
                "where table_id = ?"
        val result: Int
        connection.prepareStatement(sql).use { statement ->
            statement.setInt(1, table.id)
            statement.executeQuery().use { set ->
                result = set.getInt("count")
            }
        }
        return@withContext result
    }

    override suspend fun getAllTables(user: User): List<Table> = withContext(Dispatchers.IO) {
        if (!::connection.isInitialized)
            createDao()
        val result: MutableList<Table> = ArrayList()
        try {
            val sql = "select id, name, admin_email\n" +
                    "from tables\n" +
                    "join user_table\n" +
                    "on tables.id = user_table.table_id\n" +
                    "where user_table.user_email = ?\n" +
                    "order by id\n"
            connection.prepareStatement(sql).use { statement ->
                // Set user's email for SQL-request
                statement.setString(1, user.email)
                statement.executeQuery().use { set ->
                    // Get all found tables
                    while (set.next()) {
                        val id = set.getInt("id")
                        val name = set.getString("name")
                        val adminEmail = set.getString("admin_email")
                        result.add(
                            Table(
                                id,
                                name,
                                adminEmail
                            )
                        )
                    }
                }
            }
            lastTables = result
            roomRepository.clearTables().let {
                roomRepository.createTable(*result.toTypedArray())
            }
            Log.d("TABLES", result.toString())
            result
        } catch (e: Exception) {
            noConnectionFlag.value++
            return@withContext roomRepository.getAllTables()
        }
    }

    override suspend fun getAllPosts(table: Table): List<Post> = withContext(Dispatchers.IO) {
        val result: MutableList<Post> = ArrayList()
        val sql = "select *\n" +
                "from posts\n" +
                "where table_id = ?\n" +
                "order by date\n"
        try {

            connection.prepareStatement(sql).use { statement ->
                // Set table's id for SQL-request
                statement.setInt(1, table.id)
                statement.executeQuery().use { set ->
                    // Get all found posts
                    while (set.next()) {
                        val id = set.getInt("id")
                        val name = set.getString("name")
                        val description = set.getString("description")
                        val date = set.getString("date")
                        val colour = set.getString("colour")
                        val tableId = set.getInt("table_id")
                        val tableName = lastTables?.last { it.id == tableId }?.name ?: ""
                        result.add(
                            Post(
                                id,
                                name,
                                description,
                                date,
                                colour,
                                tableId,
                                tableName
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            noConnectionFlag.value++
            return@withContext roomRepository.getAllPostForTable(table)
        }
        return@withContext result

    }

    override suspend fun getAllUserPosts(user: User): List<Post?> = withContext(Dispatchers.IO) {
        if (!::connection.isInitialized)
            createDao()
        val result: MutableList<Post> = ArrayList()
        val sql = "select id, name, description, date, colour, table_id\n" +
                "from\n" +
                "(\n" +
                "    select id, name, description, date, colour, p.table_id\n" +
                "    from\n" +
                "    (\n" +
                "        select table_id\n" +
                "        from user_table\n" +
                "        where user_email = ?\n" +
                "    ) as t\n" +
                "    join posts as p\n" +
                "    on (t.table_id = p.table_id)\n" +
                ") as res ORDER by id"
        var statement: Statement? = null
        var set: ResultSet? = null
        try {
            statement = connection.prepareStatement(sql)
            // Set table's id for SQL-request
            statement.setString(1, user.email)
            set = statement.executeQuery()
            // Get all found posts
            while (set.next()) {
                val id = set.getInt("id")
                val name = set.getString("name")
                val description = set.getString("description")
                val date = set.getString("date")
                val colour = set.getString("colour")
                val tableId = set.getInt("table_id")
                val tableName = lastTables?.last { it.id == tableId }?.name ?: ""
                result.add(
                    Post(
                        id,
                        name,
                        description,
                        date,
                        colour,
                        tableId,
                        tableName
                    )
                )

            }
        } catch (e: Exception) {
            Log.e("ALO getAllUserPosts", e.message ?: e.toString())
            return@withContext roomRepository.getAllPosts()
        } finally {
            statement?.close()
            set?.close()
        }
        roomRepository.clearPosts().let {
            roomRepository.createPost(*result.toTypedArray())
        }
        return@withContext result
    }

    /**
     * Check user for exists
     * @return Found user if exists, else null
     */
    private suspend fun isUserExists(email: String): User? = withContext(Dispatchers.IO) {
        val sql = "select *\n" +
                "from users\n" +
                "where email = ?\n"
        connection.prepareStatement(sql).use { statement ->
            // Set user's email for SQL-request
            statement.setString(1, email)
            statement.executeQuery().use { set ->
                if (set.next()) {
                    val password = set.getString("password")
                    return@use User(email, password)
                }
                return@use null
            }
        }
    }

    private fun hashPassword(): String { // FIXME
        throw UnsupportedOperationException()
    }

    private fun createDao() {
        try {
            val properties = Properties()
            val connection = DriverManager.getConnection(
                "jdbc:postgresql://ec2-52-18-116-67.eu-west-1.compute.amazonaws.com:5432/d12t7uklg6tdge",
                "aetcjwocvbamwr",
                "f8d97b0b3f9b5652ae0169c1ffcff9fde01ca59ef187874ee7025a13c8b9b815"
            )
            this.connection = connection
            this.properties = properties
        } catch (e: Exception) {
            noConnectionFlag.value++
        }
    }

    override fun logout() {
        currentUserFlow.value = DENY_USER
    }

}
