package com.example.makeMyBudget.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_diary")
data class User(
    @PrimaryKey(autoGenerate = false) var user_id: String,

    val name: String,
    val budget: Double,

    )
