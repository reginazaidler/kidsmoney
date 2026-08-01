package com.example.kidsmoney.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: TransactionEntity): Long
}
