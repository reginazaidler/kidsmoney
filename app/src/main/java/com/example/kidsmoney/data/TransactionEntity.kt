package com.example.kidsmoney.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [ForeignKey(
        entity = ChildEntity::class,
        parentColumns = ["id"],
        childColumns = ["childId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("childId")],
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val childId: Long,
    val moneyType: String,
    val source: String,
    val client: String,
    val amount: Long,
    val transactionType: String,
    val createdAt: Long,
)
