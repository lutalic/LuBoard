package com.lutalic.luboard.model.database.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Posts exist inseparably from tables
 * One table can have many posts
 */
@Parcelize
data class Post(
    var id: Int,
    var name: String,
    var description: String,
    val date: String,
    var colour: String,
    val tableId: Int,
    val tableName: String
) : Parcelable
