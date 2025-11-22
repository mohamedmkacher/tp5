package com.example.tp5.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tp5.data.model.Etudiant


@Dao
interface EtudiantDao {

    @Query("SELECT * FROM etudiant_table ORDER BY id DESC")
    fun getAllEtudiants(): LiveData<List<Etudiant>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(etudiant: Etudiant)

    @Delete
    suspend fun delete(etudiant: Etudiant)

    @Update
    suspend fun update(etudiant: Etudiant)

    @Query("SELECT * FROM etudiant_table WHERE id = :id")
    fun getEtudiantById(id: Int): LiveData<Etudiant?>

    @Query("SELECT * FROM etudiant_table WHERE classe LIKE '%' || :classe || '%' ORDER BY id DESC")
    fun searchByClasse(classe: String): LiveData<List<Etudiant>>

    @Query("DELETE FROM etudiant_table")
    suspend fun deleteAll()
}