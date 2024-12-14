package com.example.motivation
import androidx.room.*

@Dao
interface FavoriteQuoteDao {
    @Insert
    suspend fun insertFavorite(favoriteQuote: FavoriteQuote)

    @Query("SELECT * FROM favorite_quotes")
    suspend fun getAllFavorites(): List<FavoriteQuote>

    @Delete
    suspend fun deleteFavorite(favoriteQuote: FavoriteQuote)
}
