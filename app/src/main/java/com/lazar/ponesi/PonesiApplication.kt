package com.lazar.ponesi

import android.app.Application
import com.lazar.ponesi.data.database.AppDatabase
import com.lazar.ponesi.data.location.CurrentLocationProvider
import com.lazar.ponesi.data.repository.DocumentRepository
import com.lazar.ponesi.data.repository.TravelHistoryRepository
import com.lazar.ponesi.data.repository.TravelRepository
import com.lazar.ponesi.data.repository.WeatherRepository

class PonesiApplication : Application() {

    val database by lazy {
        AppDatabase.getDatabase(this)
    }

    val travelRepository by lazy {
        TravelRepository(database)
    }

    val documentRepository by lazy {
        DocumentRepository(database)
    }

    val travelHistoryRepository by lazy {
        TravelHistoryRepository(database)
    }

    val weatherRepository by lazy {
        WeatherRepository(this)
    }

    val currentLocationProvider by lazy {
        CurrentLocationProvider(this)
    }
}