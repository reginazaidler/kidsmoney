package com.example.kidsmoney.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "children")
data class ChildEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
)
