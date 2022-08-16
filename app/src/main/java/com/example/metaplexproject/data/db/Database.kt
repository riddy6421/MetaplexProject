package com.example.metaplexproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.metaplexproject.model.NFT

@Database(
    entities = [NFT::class],
    version = 1
)
abstract class Database: RoomDatabase() {
    abstract val dataDao: DataDao
}