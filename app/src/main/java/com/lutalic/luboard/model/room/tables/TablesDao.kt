package com.lutalic.luboard.model.room.tables

import androidx.room.*

@Dao
interface TablesDao {
    @Query("SELECT * FROM tables ORDER by id")
    suspend fun getAll(): List<TableEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg tableEntity: TableEntity)

    @Delete
    suspend fun deleteTable(tableEntity: TableEntity)

    @Query("DELETE FROM tables")
    suspend fun clearTables()
}