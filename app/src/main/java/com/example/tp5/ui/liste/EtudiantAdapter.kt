package com.example.tp5.ui.liste

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp5.data.model.Etudiant
import com.example.tp5.databinding.ItemEtudiantBinding

class EtudiantAdapter(
    private val onItemClick: (Etudiant) -> Unit,
    private val onItemLongClick: (Etudiant) -> Unit,
    private val onItemUpdate: (Etudiant) -> Unit
) : RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder>() {

    private var etudiants = listOf<Etudiant>()

    inner class EtudiantViewHolder(private val binding: ItemEtudiantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(etudiant: Etudiant) {
            binding.apply {
                tvEmail.text = etudiant.mail
                tvClasse.text = etudiant.classe

                // Get initial from email
                val initial = etudiant.mail.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
                tvInitial.text = initial

                // Click listeners
                root.setOnClickListener { onItemClick(etudiant) }
                root.setOnLongClickListener {
                    onItemLongClick(etudiant)
                    true
                }
                btnUpdate.setOnClickListener { onItemUpdate(etudiant) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EtudiantViewHolder {
        val binding = ItemEtudiantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EtudiantViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EtudiantViewHolder, position: Int) {
        holder.bind(etudiants[position])
    }

    override fun getItemCount() = etudiants.size

    fun updateList(newList: List<Etudiant>) {
        etudiants = newList
        notifyDataSetChanged()
    }
}