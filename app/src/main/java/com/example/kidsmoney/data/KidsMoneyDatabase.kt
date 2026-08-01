package com.example.kidsmoney.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ChildEntity::class, TransactionEntity::class], version = 1, exportSchema = false)
abstract class KidsMoneyDatabase : RoomDatabase() {
    abstract fun childDao(): ChildDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        fun create(context: Context): KidsMoneyDatabase =
            Room.databaseBuilder(context, KidsMoneyDatabase::class.java, "kids_money.db")
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        db.execSQL("INSERT INTO children (name) VALUES (?), (?), (?)", arrayOf("אגם", "בן", "יאיר"))
                    }
                })
                .build()
    }
}
