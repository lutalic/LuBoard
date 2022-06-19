package com.lutalic.luboard.model.room.account

import androidx.room.*
import com.lutalic.luboard.model.room.account.AccountEntity

@Dao
interface AccountsDao {

    @Query("SELECT id, email, password FROM accounts WHERE email = :email")
    suspend fun findByEmail(email: String): AccountEntity?

    @Insert(entity = AccountEntity::class)
    suspend fun createAccount(accountEntity: AccountEntity)

    @Update(entity = AccountEntity::class)
    suspend fun updateAccount(accountEntity: AccountEntity)

    @Delete(entity = AccountEntity::class)
    suspend fun deleteAccount(accountEntity: AccountEntity)
}