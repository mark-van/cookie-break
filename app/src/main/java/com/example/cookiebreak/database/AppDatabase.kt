package com.example.cookiebreak.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(History::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun scheduleDao(): HistoryDao
    companion object {
        //keeps a reference to the database
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cookie_database")
                    .fallbackToDestructiveMigration() //maybe research a more efficient method later
                    .build()
                INSTANCE = instance

                return instance
            }
        }
    }
}