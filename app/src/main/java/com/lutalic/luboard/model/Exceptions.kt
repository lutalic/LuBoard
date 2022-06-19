package com.lutalic.luboard.model

open class AppException(message: String? = null) : RuntimeException(message)

class EmptyFieldException(
    val field: Field
) : AppException()

class AccountAlreadyExistsException : AppException()

class NoValidateEmailException : AppException()

open class AuthException(override val message: String?) : AppException(message)

class PasswordMismatchException(override val message: String?) : AuthException(message)

class NoSuchUserException(override val message: String?) : AuthException(message)
