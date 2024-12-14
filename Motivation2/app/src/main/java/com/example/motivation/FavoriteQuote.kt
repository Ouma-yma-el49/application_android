package com.example.motivation
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_quotes") // Cette annotation marque la classe comme une entité de db dans Room, qui correspond à une table.
data class FavoriteQuote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,// @PrimaryKet est une annotation désigne id comme la clé primaire de la table.
    @ColumnInfo(name = "quote") val quote: String // Cela définit le nom de la colonne pour la propriété quote dans la table.
)
