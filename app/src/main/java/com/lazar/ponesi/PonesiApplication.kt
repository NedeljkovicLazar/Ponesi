package com.lazar.ponesi

import android.app.Application
import com.lazar.ponesi.data.database.AppDatabase
import com.lazar.ponesi.data.repository.TravelRepository

class PonesiApplication : Application() {

    val database by lazy {
        AppDatabase.getDatabase(this)
    }

    val travelRepository by lazy {
        TravelRepository(database)
    }
}