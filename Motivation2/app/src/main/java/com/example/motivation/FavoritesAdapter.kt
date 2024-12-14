package com.example.motivation
import android.content.Context
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class FavoritesAdapter(
    private val context: Context,
    private val favoriteQuotes: MutableList<String>,
    private val favoriteQuoteDao: FavoriteQuoteDao) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val quote = favoriteQuotes[position]
        holder.quoteText.text = quote  // Lier la citation au TextView

        // Ajouter une action pour supprimer le favori
        holder.deleteIcon.setOnClickListener {
            // Lancer une coroutine pour supprimer le favori de la base de données
            CoroutineScope(Dispatchers.IO).launch {
                // Suppression du favori dans la base de données
                favoriteQuoteDao.deleteFavorite(FavoriteQuote(quote = quote))

                // Retourner sur le thread principal pour mettre à jour la liste des favoris
                withContext(Dispatchers.Main) {
                    // Supprimer le favori de la liste et mettre à jour l'affichage immédiatement
                    favoriteQuotes.removeAt(position)
                    notifyItemRemoved(position)  // Mettre à jour l'affichage
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteQuotes.size
    }

    // ViewHolder pour l'élément de citation
    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val quoteText: TextView = view.findViewById(R.id.quoteText)  // Assurez-vous que l'ID correspond
        val deleteIcon: ImageView = view.findViewById(R.id.deleteIcon)
    }
}
