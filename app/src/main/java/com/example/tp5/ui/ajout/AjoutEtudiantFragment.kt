package com.example.tp5.ui.ajout

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tp5.R
import com.example.tp5.data.model.Etudiant
import com.example.tp5.databinding.FragmentAjoutEtudiantBinding
import com.google.android.material.snackbar.Snackbar
import com.example.tp5.EtudiantApplication
import com.example.tp5.ui.viewmodel.EtudiantViewModel
import com.example.tp5.ui.viewmodel.EtudiantViewModelFactory

class AjoutEtudiantFragment : Fragment() {

    private var _binding: FragmentAjoutEtudiantBinding? = null
    private val binding get() = _binding!!

    private val etudiantViewModel: EtudiantViewModel by activityViewModels {
        EtudiantViewModelFactory(
            (requireActivity().application as EtudiantApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAjoutEtudiantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            saveEtudiant()
        }
    }

    private fun saveEtudiant() {
        val email = binding.etEmail.text.toString().trim()
        val classe = binding.etClasse.text.toString().trim()


        when {
            email.isEmpty() || classe.isEmpty() -> {
                Snackbar.make(
                    binding.root,
                    getString(R.string.error_empty_fields),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.emailInputLayout.error = getString(R.string.error_invalid_email)
            }
            else -> {

                binding.emailInputLayout.error = null


                val etudiant = Etudiant(mail = email, classe = classe)
                etudiantViewModel.addEtudiant(etudiant)


                Snackbar.make(
                    binding.root,
                    getString(R.string.success_add),
                    Snackbar.LENGTH_SHORT
                ).show()

                clearFields()

                findNavController().navigate(R.id.action_ajout_to_liste)
            }
        }
    }

    private fun clearFields() {
        binding.etEmail.text?.clear()
        binding.etClasse.text?.clear()
        binding.etEmail.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}