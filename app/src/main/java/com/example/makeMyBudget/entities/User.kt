package com.example.makeMyBudget.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_diary")
data class User(
    val username: String,
    @PrimaryKey(autoGenerate = true) val user_id: Long,

    )
