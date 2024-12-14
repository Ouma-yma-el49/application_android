package com.example.motivation

import retrofit2.Call
import retrofit2.http.GET

// Interface pour l'API ZenQuotes
interface QuoteApi {

    // Fonction pour récupérer une citation aléatoire
    @GET("api/random?lang=fr") //@GET est une annotation de Rertofit qui définit le type de requête HTTP
    fun getRandomQuote(): Call<List<Quote>> //cette méthode renvoie une liste de quote
}