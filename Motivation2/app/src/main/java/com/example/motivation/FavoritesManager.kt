package com.example.motivation
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FavoritesManager {
    private fun getDao(context: Context): FavoriteQuoteDao {
        return AppDatabase.getDatabase(context).favoriteQuoteDao()
    }
    // Sauvegarder un favori
    suspend fun saveFavorite(context: Context, quote: String) {
        val dao = getDao(context)
        dao.insertFavorite(FavoriteQuote(quote = quote))
    }
    // Récupérer tous les favoris
    suspend fun getFavorites(context: Context): List<String> {
        val dao = getDao(context)
        //  la fonction 'map' standard de Kotlin pour transformer la liste de FavoriteQuote en une liste de citations
        return withContext(Dispatchers.IO) {
            dao.getAllFavorites().map { it.quote }  // 'map' de Kotlin pour les listes
        }
    }
    // Supprimer un favori
    suspend fun removeFavorite(context: Context, quote: String) {
        val dao = getDao(context)
        // Trouver le favori à supprimer et appeler la méthode de suppression
        val favoriteToDelete = dao.getAllFavorites().find { it.quote == quote }
        favoriteToDelete?.let {
            withContext(Dispatchers.IO) {
                dao.deleteFavorite(it)
            }
        }
    }
}
