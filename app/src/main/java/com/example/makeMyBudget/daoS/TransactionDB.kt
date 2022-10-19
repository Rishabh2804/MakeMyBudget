package com.example.makeMyBudget.daoS

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.makeMyBudget.entities.Transaction
import com.example.makeMyBudget.entities.User

@Database(entities = [User::class, Transaction::class], version = 2)
abstract class TransactionDB : RoomDatabase() {
    abstract fun transactionDao(): TransactionManager
    abstract fun userDao(): UserAgent
    abstract fun listHandlerDao(): ListHandler

    companion object {
        @Volatile
        private var instance: TransactionDB? = null

        fun getDatabase(context: Context) = instance
            ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDB::class.java,
                    "transaction_db"
                ).addMigrations(MIGRATION_1_2)
                    .build().also { instance = it }
            }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE user_diary_2 (username TEXT not null, budget TEXT not null, user_id TEXT NOT NULL ,primary key (user_id) )")
                database.execSQL("DROP TABLE user_diary")
                database.execSQL("ALTER TABLE user_diary_2 rename to user_diary")
            }
        }

    }
}