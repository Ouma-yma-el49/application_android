package com.example.motivation
import android.app.*
import android.content.*
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation des vues
        quoteTextView = findViewById(R.id.quoteTextView)
        authorTextView = findViewById(R.id.authorTextView)
        shareIcon = findViewById(R.id.shareIcon)
        favoriteIcon = findViewById(R.id.favoriteIcon)
        favoritesButton = findViewById(R.id.favoritesButton)
        selectTimeButton = findViewById(R.id.selectTimeButton)
    // Ajout du clic sur la carte de la citation
        val quoteCard: RelativeLayout = findViewById(R.id.quoteCard)
        quoteCard.setOnClickListener {
            fetchRandomQuote() // Générer une nouvelle citation
        }

        // Récupérer une citation aléatoire
        fetchRandomQuote()

        // Partage de citation
        shareIcon.setOnClickListener { shareQuote() }

        // Navigation vers l'écran des favoris
        favoritesButton.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        // Ajouter/supprimer des favoris
        favoriteIcon.setOnClickListener { toggleFavorite() }

        // Sélection de l'heure pour l'alarme
        selectTimeButton.setOnClickListener { showTimePicker() }
    }
    private lateinit var shareIcon: ImageView
    private lateinit var favoriteIcon: ImageView
    private lateinit var favoritesButton: Button
    private lateinit var selectTimeButton: ImageView
    // Récupérer une citation depuis l'API
    private fun fetchRandomQuote() {
        RetrofitInstance.quoteApi.getRandomQuote().enqueue(object : Callback<List<Quote>> {
            override fun onResponse(call: Call<List<Quote>>, response: Response<List<Quote>>) {
                if (response.isSuccessful) {
                    response.body()?.firstOrNull()?.let {
                        quoteTextView.text = "\"${it.q}\""
                        authorTextView.text = "- ${it.a}"
                    } ?: showError("Citation non trouvée")
                } else {
                    showError("Erreur : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Quote>>, t: Throwable) {
                showError("Erreur réseau")
            }
        })
    }

    // Afficher un message d'erreur
    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Partager la citation
    private fun shareQuote() {
        val shareText = "${quoteTextView.text}\n${authorTextView.text}"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        startActivity(Intent.createChooser(shareIntent, "Partager via"))
    }

    // Gérer les favoris
    private fun toggleFavorite() {
        val currentQuote = quoteTextView.text.toString()
        lifecycleScope.launch {
            val currentFavorites = FavoritesManager.getFavorites(this@MainActivity).toMutableList()

            if (!currentFavorites.contains(currentQuote)) {

                FavoritesManager.saveFavorite(this@MainActivity, currentQuote)
                Toast.makeText(this@MainActivity, "Ajouté aux favoris", Toast.LENGTH_SHORT).show()
                favoriteIcon.setImageResource(R.drawable.coeur)  // Cœur rouge
            } else {
                FavoritesManager.removeFavorite(this@MainActivity, currentQuote)
                Toast.makeText(this@MainActivity, "Déjà dans les favoris", Toast.LENGTH_SHORT)
                    .show()
                favoriteIcon.setImageResource(R.drawable.coeur)  // Cœur gris
            }
        }
    }

    // Afficher le TimePickerDialog
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(this, { _, hourOfDay, minute ->
            scheduleNotificationAt(hourOfDay, minute)
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    // Programmer la notification à une heure précise
    private fun scheduleNotificationAt(hourOfDay: Int, minute: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(this, "Notification programmée à ${hourOfDay}h${minute}", Toast.LENGTH_SHORT).show()
    }
}
