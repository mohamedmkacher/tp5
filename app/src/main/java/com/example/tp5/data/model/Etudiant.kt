package com.example.tp5.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "etudiant_table")
data class Etudiant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mail: String,
    val classe: String
)