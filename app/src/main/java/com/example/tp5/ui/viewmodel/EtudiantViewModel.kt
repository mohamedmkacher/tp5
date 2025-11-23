package com.example.tp5.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp5.data.model.Etudiant

import com.example.tp5.data.repository.EtudiantRepository
import kotlinx.coroutines.launch

class EtudiantViewModel(private val repository: EtudiantRepository) : ViewModel() {


    val allEtudiants: LiveData<List<Etudiant>> = repository.allEtudiants


    private val _searchResults = MutableLiveData<List<Etudiant>>()
    val searchResults: LiveData<List<Etudiant>> = _searchResults


    private val _selectedEtudiant = MutableLiveData<Etudiant?>()
    val selectedEtudiant: LiveData<Etudiant?> = _selectedEtudiant


    fun addEtudiant(etudiant: Etudiant) {
        viewModelScope.launch {
            repository.insert(etudiant)
        }
    }


    fun deleteEtudiant(etudiant: Etudiant) {
        viewModelScope.launch {
            repository.delete(etudiant)
        }
    }


    fun updateEtudiant(etudiant: Etudiant) {
        viewModelScope.launch {
            repository.update(etudiant)
        }
    }


    fun selectEtudiant(etudiant: Etudiant?) {
        _selectedEtudiant.value = etudiant
    }


    fun searchByClasse(classe: String) {
        viewModelScope.launch {
            val results = repository.searchByClasse(classe)
            results.observeForever { list ->
                _searchResults.postValue(list)
            }
        }
    }


    fun clearSearch() {
        _searchResults.value = emptyList()
    }
}