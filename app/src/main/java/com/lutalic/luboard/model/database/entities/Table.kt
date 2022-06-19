package com.lutalic.luboard.model.database.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Entity describing the table
 * One user can have many tables
 * One table can have many posts and single user as administrator
 */
@Parcelize
data class Table(
    var id: Int,
    var name: String,
    var admin: String
) : Parcelable
