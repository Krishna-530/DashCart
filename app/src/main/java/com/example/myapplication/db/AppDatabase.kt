package com.example.myapplication.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * AppDatabase — Room database singleton for the entire app.
 *
 * Current tables:
 *  - orders (OrderEntity) — persists placed order history
 *
 * Bump [version] whenever you change the schema. Add a Migration if you
 * need to preserve existing data; use fallbackToDestructiveMigration for dev builds.
 */
@Database(entities = [OrderEntity::class, NotificationEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun orderDao(): OrderDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /** Returns the singleton database instance, creating it if necessary. */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "grocery_app_db"
                )
                    // Allow destructive migration during development
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
