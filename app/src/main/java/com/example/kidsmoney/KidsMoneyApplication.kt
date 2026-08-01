package com.example.kidsmoney

import android.app.Application
import com.example.kidsmoney.data.KidsMoneyDatabase
import com.example.kidsmoney.data.KidsMoneyRepository

class KidsMoneyApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}

class AppContainer(application: Application) {
    private val database = KidsMoneyDatabase.create(application)
    val repository = KidsMoneyRepository(database.childDao(), database.transactionDao())
}
