package com.example.starclicker.dialogs.boost

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.starclicker.ViewModelFactory
import com.example.starclicker.database.StarClickerDatabase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

class BoostDialog private constructor() : DialogFragment(){
    private lateinit var viewModel: BoostViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val datasource = StarClickerDatabase.getInstance(requireActivity().application).databaseDao
        val viewModelFactory = ViewModelFactory(datasource)

        viewModel = ViewModelProvider(this, viewModelFactory)[BoostViewModel::class.java]

        viewModel.boosters.observe(this, { list ->
            // this may fire up twice. Make sure it's not a problem
            Timber.e("$list")
            // adapter.submitList(list)
        })

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Boosty")
            .setMessage("Tutaj powinna być lista z dostępnymi boostami")
            .setNegativeButton("Ok") { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton("Anuluj") { dialog, which ->
                // Respond to positive button press
            }
            .create()
    }

    companion object {
        fun newInstance(): BoostDialog {
            return BoostDialog()
        }
    }
}

