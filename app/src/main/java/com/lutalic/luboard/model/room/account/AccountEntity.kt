package com.lutalic.luboard.model.room.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "accounts"
)
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val email: String,
    val password: String
)
