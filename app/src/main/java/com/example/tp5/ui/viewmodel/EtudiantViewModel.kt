package com.example.tp5.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp5.data.model.Etudiant

import com.example.tp5.data.repository.EtudiantRepository
import kotlinx.coroutines.launch

class EtudiantViewModel(private val repository: EtudiantRepository) : ViewModel() {

    // All students from database
    val allEtudiants: LiveData<List<Etudiant>> = repository.allEtudiants

    // Search results
    private val _searchResults = MutableLiveData<List<Etudiant>>()
    val searchResults: LiveData<List<Etudiant>> = _searchResults

    // Selected student for details
    private val _selectedEtudiant = MutableLiveData<Etudiant?>()
    val selectedEtudiant: LiveData<Etudiant?> = _selectedEtudiant

    // Add student
    fun addEtudiant(etudiant: Etudiant) {
        viewModelScope.launch {
            repository.insert(etudiant)
        }
    }

    // Delete student
    fun deleteEtudiant(etudiant: Etudiant) {
        viewModelScope.launch {
            repository.delete(etudiant)
        }
    }

    // Update student
    fun updateEtudiant(etudiant: Etudiant) {
        viewModelScope.launch {
            repository.update(etudiant)
        }
    }

    // Select student
    fun selectEtudiant(etudiant: Etudiant?) {
        _selectedEtudiant.value = etudiant
    }

    // Search by class
    fun searchByClasse(classe: String) {
        viewModelScope.launch {
            val results = repository.searchByClasse(classe)
            results.observeForever { list ->
                _searchResults.postValue(list)
            }
        }
    }

    // Clear search
    fun clearSearch() {
        _searchResults.value = emptyList()
    }
}