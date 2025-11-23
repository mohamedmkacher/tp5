package com.example.tp5.ui.liste

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tp5.R
import com.example.tp5.data.model.Etudiant
import com.example.tp5.databinding.FragmentListeEtudiantsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.example.tp5.EtudiantApplication

import com.example.tp5.ui.viewmodel.EtudiantViewModel
import com.example.tp5.ui.viewmodel.EtudiantViewModelFactory

class ListeEtudiantsFragment : Fragment() {

    private var _binding: FragmentListeEtudiantsBinding? = null
    private val binding get() = _binding!!

    private val etudiantViewModel: EtudiantViewModel by activityViewModels {
        EtudiantViewModelFactory(
            (requireActivity().application as EtudiantApplication).repository
        )
    }

    private lateinit var adapter: EtudiantAdapter
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListeEtudiantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeData()
        setupMenu()
    }

    private fun setupRecyclerView() {
        adapter = EtudiantAdapter(
            onItemClick = { etudiant ->
                etudiantViewModel.selectEtudiant(etudiant)
                findNavController().navigate(R.id.action_liste_to_details)
            },
            onItemLongClick = { etudiant ->
                showDeleteConfirmation(etudiant)
            },
            onItemUpdate = { etudiant ->
                showUpdateDialog(etudiant)
            }
        )

        binding.recyclerViewEtudiants.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ListeEtudiantsFragment.adapter
        }
    }

    private fun observeData() {

        etudiantViewModel.allEtudiants.observe(viewLifecycleOwner) { etudiants ->
            if (!isSearching) {
                updateUI(etudiants)
            }
        }


        etudiantViewModel.searchResults.observe(viewLifecycleOwner) { results ->
            if (isSearching && results.isNotEmpty()) {
                updateUI(results)
            }
        }
    }

    private fun updateUI(etudiants: List<Etudiant>) {
        if (etudiants.isEmpty()) {
            binding.recyclerViewEtudiants.visibility = View.GONE
            binding.emptyState.visibility = View.VISIBLE
        } else {
            binding.recyclerViewEtudiants.visibility = View.VISIBLE
            binding.emptyState.visibility = View.GONE
            adapter.updateList(etudiants)
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem?.actionView as? SearchView

                searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText.isNullOrEmpty()) {
                            isSearching = false
                            etudiantViewModel.clearSearch()
                        } else {
                            isSearching = true
                            etudiantViewModel.searchByClasse(newText)
                        }
                        return true
                    }
                })

                searchView?.setOnCloseListener {
                    isSearching = false
                    etudiantViewModel.clearSearch()
                    false
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showDeleteConfirmation(etudiant: Etudiant) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Student")
            .setMessage(getString(R.string.confirm_delete))
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                etudiantViewModel.deleteEtudiant(etudiant)
                Snackbar.make(
                    binding.root,
                    getString(R.string.success_delete),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    private fun showUpdateDialog(etudiant: Etudiant) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_edit_details, null)

        val etEmail = dialogView.findViewById<TextInputEditText>(R.id.etDialogEmail)
        val etClasse = dialogView.findViewById<TextInputEditText>(R.id.etDialogClasse)

        etEmail.setText(etudiant.mail)
        etClasse.setText(etudiant.classe)

        AlertDialog.Builder(requireContext())
            .setTitle("Update Student")
            .setView(dialogView)
            .setPositiveButton(getString(R.string.update)) { _, _ ->
                val newEmail = etEmail.text.toString().trim()
                val newClasse = etClasse.text.toString().trim()

                if (newEmail.isNotEmpty() && newClasse.isNotEmpty()) {
                    val updatedEtudiant = etudiant.copy(
                        mail = newEmail,
                        classe = newClasse
                    )
                    etudiantViewModel.updateEtudiant(updatedEtudiant)
                    Snackbar.make(
                        binding.root,
                        getString(R.string.success_update),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}