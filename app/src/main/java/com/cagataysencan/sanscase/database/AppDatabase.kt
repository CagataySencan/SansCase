package com.cagataysencan.sanscase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cagataysencan.sanscase.constant.Constants
import com.cagataysencan.sanscase.model.Match

@Database(entities = [Match::class], version = Constants.DATABASE_VERSION, exportSchema = false)
@TypeConverters(DatabaseTypeConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getMatchDao(): MatchDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase? {
            if(INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java,
                        name = Constants.DATABASE_NAME)
                        .build()
                }
            }
            return INSTANCE
        }
    }
}