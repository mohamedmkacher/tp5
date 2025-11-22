package com.example.tp5.data.repository

import androidx.lifecycle.LiveData
import com.example.tp5.data.dao.EtudiantDao
import com.example.tp5.data.model.Etudiant


class EtudiantRepository(private val etudiantDao: EtudiantDao) {

    val allEtudiants: LiveData<List<Etudiant>> = etudiantDao.getAllEtudiants()

    suspend fun insert(etudiant: Etudiant) {
        etudiantDao.insert(etudiant)
    }

    suspend fun delete(etudiant: Etudiant) {
        etudiantDao.delete(etudiant)
    }

    suspend fun update(etudiant: Etudiant) {
        etudiantDao.update(etudiant)
    }

    fun getEtudiantById(id: Int): LiveData<Etudiant?> {
        return etudiantDao.getEtudiantById(id)
    }

    fun searchByClasse(classe: String): LiveData<List<Etudiant>> {
        return etudiantDao.searchByClasse(classe)
    }

    suspend fun deleteAll() {
        etudiantDao.deleteAll()
    }
}