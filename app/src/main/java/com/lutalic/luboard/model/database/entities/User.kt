package com.lutalic.luboard.model.database.entities

/**
 * Information about current user, updated only on user change
 * Automatically entered into the database upon successful registration
 */
data class User(val email: String, val password: String)
