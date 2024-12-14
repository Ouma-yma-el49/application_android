package com.example.motivation
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import kotlinx.coroutines.launch

class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoris)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewFavorites)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Récupérer le DAO pour l'adaptateur
        val favoriteQuoteDao = AppDatabase.getDatabase(this).favoriteQuoteDao()

        // Récupérer les favoris sous forme de liste d'objets Quote
        lifecycleScope.launch {
            val favoriteQuotes = FavoritesManager.getFavorites(this@FavoritesActivity).toMutableList()
            // Configurer l'adaptateur avec le DAO
            val adapter = FavoritesAdapter(this@FavoritesActivity, favoriteQuotes, favoriteQuoteDao)
            recyclerView.adapter = adapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Utilisez le dispatcher pour gérer le retour
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
