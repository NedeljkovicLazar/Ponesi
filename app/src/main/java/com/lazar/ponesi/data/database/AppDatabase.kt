package com.lazar.ponesi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lazar.ponesi.data.database.converter.DatabaseConverters
import com.lazar.ponesi.data.database.dao.DocumentDao
import com.lazar.ponesi.data.database.dao.TravelDao
import com.lazar.ponesi.data.database.dao.TravelHistoryDao
import com.lazar.ponesi.data.database.entity.CategoryEntity
import com.lazar.ponesi.data.database.entity.DocumentEntity
import com.lazar.ponesi.data.database.entity.PackingItemEntity
import com.lazar.ponesi.data.database.entity.TravelEntity
import com.lazar.ponesi.data.database.entity.TravelHistoryEntity

@Database(
    entities = [
        TravelEntity::class,
        CategoryEntity::class,
        PackingItemEntity::class,
        DocumentEntity::class,
        TravelHistoryEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun travelDao(): TravelDao

    abstract fun documentDao(): DocumentDao

    abstract fun travelHistoryDao(): TravelHistoryDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2 =
            object : Migration(1, 2) {

                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {
                    db.execSQL(
                        """
                        CREATE TABLE IF NOT EXISTS documents (
                            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            title TEXT NOT NULL,
                            uri TEXT NOT NULL,
                            mimeType TEXT NOT NULL
                        )
                        """.trimIndent()
                    )
                }
            }

        private val MIGRATION_2_3 =
            object : Migration(2, 3) {

                override fun migrate(
                    db: SupportSQLiteDatabase
                ) {
                    db.execSQL(
                        "ALTER TABLE travels " +
                                "ADD COLUMN startDate TEXT"
                    )

                    db.execSQL(
                        "ALTER TABLE travels " +
                                "ADD COLUMN locationName TEXT"
                    )

                    db.execSQL(
                        "ALTER TABLE travels " +
                                "ADD COLUMN locationLatitude REAL"
                    )

                    db.execSQL(
                        "ALTER TABLE travels " +
                                "ADD COLUMN locationLongitude REAL"
                    )

                    db.execSQL(
                        """
                        CREATE TABLE IF NOT EXISTS travel_history (
                            id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            title TEXT NOT NULL,
                            locationName TEXT,
                            startDate TEXT NOT NULL,
                            endDate TEXT NOT NULL,
                            photoUri TEXT
                        )
                        """.trimIndent()
                    )
                }
            }

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ponesi_database"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3
                    )
                    .addCallback(InitialDataCallback)
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}