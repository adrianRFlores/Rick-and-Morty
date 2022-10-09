package com.example.laboratorio8

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CharDao {
    @Query("SELECT * FROM character")
    suspend fun getChars(): List<Character>

    @Query("SELECT * FROM character WHERE id = :id")
    suspend fun getChar(id:Int): Character

    @Insert
    suspend fun insert(character: Character)

    @Update
    suspend fun update(character: Character)

    @Delete
    suspend fun delete(character: Character): Int

    @Query("DELETE FROM character")
    suspend fun deleteAll(): Int
}