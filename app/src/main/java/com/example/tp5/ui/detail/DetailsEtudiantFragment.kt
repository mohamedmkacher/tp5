package com.example.tp5.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tp5.databinding.FragmentDetailEtudiantBinding
import com.example.tp5.EtudiantApplication
import com.example.tp5.ui.viewmodel.EtudiantViewModel
import com.example.tp5.ui.viewmodel.EtudiantViewModelFactory

class DetailsEtudiantFragment : Fragment() {

    private var _binding: FragmentDetailEtudiantBinding? = null
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
        _binding = FragmentDetailEtudiantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etudiantViewModel.selectedEtudiant.observe(viewLifecycleOwner) { etudiant ->
            etudiant?.let {
                binding.apply {
                    tvDetailsId.text = "#${it.id}"
                    tvDetailsEmail.text = it.mail
                    tvDetailsClasse.text = it.classe

                    // Get initial from email
                    val initial = it.mail.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
                    tvInitial.text = initial
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}