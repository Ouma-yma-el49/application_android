package com.example.motivation

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

// BroadcastReceiver pour recevoir l'événement d'alarme et afficher une notification
class NotificationReceiver : BroadcastReceiver() {

    // Cette méthode est appelée lorsque le BroadcastReceiver reçoit un message
    override fun onReceive(context: Context, intent: Intent) {
        // Citation motivante à afficher dans la notification
        val quote = "La persévérance est la clé du succès."

        // Appel de la méthode pour afficher la notification
        showNotification(context, quote)
    }

    // Fonction pour afficher la notification
    private fun showNotification(context: Context, quote: String) {
        val channelId = "daily_quote_channel" // ID du canal de notification
        val notificationId = 1 // ID unique pour cette notification

        // Création d'un PendingIntent pour ouvrir MainActivity lorsque l'utilisateur clique sur la notification
        val pendingIntent = PendingIntent.getActivity(
            context, 0, Intent(context, MainActivity::class.java), // Intent pour MainActivity
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Flags pour mettre à jour ou garder l'intent immuable
        )

        // Construction de la notification avec tous les paramètres nécessaires
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.notif) // Icône de la notification
            .setContentTitle("✨ Citation du jour ✨") // Titre de la notification
            .setContentText("Commencez votre journée avec cette dose de motivation !  \uD83D\uDCAA ") // Texte principal de la notification
            .setStyle(NotificationCompat.BigTextStyle().bigText(quote)) // Style pour afficher le texte long correctement
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Priorité de la notification (haute)
            .setContentIntent(pendingIntent) // L'action à faire lorsque l'utilisateur clique sur la notification
            .setAutoCancel(true) // La notification sera supprimée après avoir été cliquée
            .build() // Construction de l'objet notification final

        // Récupération du NotificationManager pour afficher la notification
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager)
            .notify(notificationId, notification) // Affichage de la notification avec l'ID et l'objet notification
    }
}
