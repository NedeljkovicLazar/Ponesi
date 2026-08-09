package com.lazar.ponesi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lazar.ponesi.data.database.converter.DatabaseConverters
import com.lazar.ponesi.data.database.dao.TravelDao
import com.lazar.ponesi.data.database.entity.CategoryEntity
import com.lazar.ponesi.data.database.entity.PackingItemEntity
import com.lazar.ponesi.data.database.entity.TravelEntity

@Database(
    entities = [
        TravelEntity::class,
        CategoryEntity::class,
        PackingItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun travelDao(): TravelDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ponesi_database"
                ).build()

                INSTANCE = instance

                instance
            }
        }
    }
}