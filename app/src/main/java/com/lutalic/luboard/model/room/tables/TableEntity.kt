package com.lutalic.luboard.model.room.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lutalic.luboard.model.database.entities.Table

@Entity(
    tableName = "tables"
)
data class TableEntity(
    @PrimaryKey var id: Int,
    var name: String,
    var admin: String
) {
    fun toTable() = Table(id, name, admin)

    companion object {
        fun tableToTableEntity(table: Table) = TableEntity(
            id = table.id,
            name = table.name,
            admin = table.admin
        )


    }

    fun tableEntityToTable()= Table(
        id = this.id,
        name = this.name,
        admin = this.admin
    )
}
