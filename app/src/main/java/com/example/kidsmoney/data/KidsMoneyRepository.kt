package com.example.kidsmoney.data

import kotlinx.coroutines.flow.Flow

class KidsMoneyRepository(
    private val childDao: ChildDao,
    private val transactionDao: TransactionDao,
) {
    val childrenWithBalances: Flow<List<ChildBalance>> = childDao.observeChildrenWithBalances()

    suspend fun addIncome(
        childId: Long,
        moneyType: String,
        source: String,
        client: String,
        amount: Long,
    ) {
        transactionDao.insert(
            TransactionEntity(
                childId = childId,
                moneyType = moneyType,
                source = source,
                client = client,
                amount = amount,
                transactionType = "INCOME",
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}
