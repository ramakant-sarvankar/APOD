package com.gs.apod.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gs.apod.repository.local.dao.ImagesDAO
import com.gs.apod.repository.local.tables.Images

@Database(entities = [Images::class], version = 1)
abstract class APODDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: APODDatabase? = null

        fun getInstance(context: Context): APODDatabase? {
            if (INSTANCE == null) {
                synchronized(APODDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        APODDatabase::class.java, "apod.db"
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
    abstract fun imagesDao() : ImagesDAO
}