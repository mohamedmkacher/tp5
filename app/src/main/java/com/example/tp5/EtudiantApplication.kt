package com.example.tp5

import android.app.Application
import com.example.tp5.data.database.EtudiantDatabase
import com.example.tp5.data.repository.EtudiantRepository

class EtudiantApplication : Application() {

    val database by lazy { EtudiantDatabase.getDatabase(this) }
    val repository by lazy { EtudiantRepository(database.etudiantDao()) }
}