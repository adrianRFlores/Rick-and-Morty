package com.example.laboratorio8

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Character::class], version = 1)
abstract class Database : RoomDatabase(){
    abstract fun getChar(): CharDao
}
