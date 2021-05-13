package com.google.imtoken.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * description ：
 * author : Liszt
 * date : 2021/5/13 14:25
 */


@Database(
    entities = arrayOf(CsvBean::class),
    version = 1,
    exportSchema = true
)

abstract class AppDataBase : RoomDatabase() {

    abstract fun csvDao(): CsvDao

    companion object {
        private const val DB_NAME = "flag_db"

        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun initDB(context: Context) {
            Log.i("DB", "initDB")
            synchronized(this) {
                try {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java,
                        DB_NAME
                    ).fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
//                    .addMigrations(MIGRATION_1_2)
                        .build()
                    INSTANCE = instance
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun getDataBase(): AppDataBase? {
            return INSTANCE
        }


    }
}