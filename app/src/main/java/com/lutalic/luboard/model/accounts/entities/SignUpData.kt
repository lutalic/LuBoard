package com.lutalic.luboard.model.accounts.entities

import com.lutalic.luboard.model.EmptyFieldException
import com.lutalic.luboard.model.Field
import com.lutalic.luboard.model.PasswordMismatchException

/**
 * Fields that should be provided during creating a new account.
 */
data class SignUpData(
    val email: String,
    val password: String,
    val repeatPassword: String
) {
    fun validate() {
        if (email.isBlank()) {
            throw EmptyFieldException(Field.Email)
        }
        if (password.isBlank()) {
            throw EmptyFieldException(Field.Password)
        }
        if (password != repeatPassword) {
            throw PasswordMismatchException("PasswordMismatchException")
        }
    }
}