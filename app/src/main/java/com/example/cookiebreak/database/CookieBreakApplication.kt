package com.example.cookiebreak.database

import android.app.Application

class CookieBreakApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}