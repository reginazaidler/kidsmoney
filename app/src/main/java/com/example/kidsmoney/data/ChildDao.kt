package com.example.kidsmoney.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class ChildBalance(
    val id: Long,
    val name: String,
    val balance: Long,
)

@Dao
interface ChildDao {
    @Query("""
        SELECT children.id, children.name, COALESCE(SUM(transactions.amount), 0) AS balance
        FROM children LEFT JOIN transactions ON children.id = transactions.childId
        GROUP BY children.id, children.name ORDER BY children.id
    """)
    fun observeChildrenWithBalances(): Flow<List<ChildBalance>>
}
