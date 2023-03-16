package com.example.myapplicationg.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplicationg.data.db.dao.PublishItemDao
import com.example.myapplicationg.data.db.dao.RemoteKeyDao
import com.example.myapplicationg.data.db.entity.PublishItem
import com.example.myapplicationg.data.db.entity.RemoteKey

@Database(entities = [PublishItem::class, RemoteKey::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class DbDataSource : RoomDatabase() {
    abstract fun publishItemDao(): PublishItemDao

    abstract fun remoteKeyDao(): RemoteKeyDao
}