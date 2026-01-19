package com.example.expensetracker.data.local

import com.example.expensetracker.data.local.ExpenseDao
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun expenseDao(): ExpenseDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{

            return INSTANCE ?: synchronized(this){

                var instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}