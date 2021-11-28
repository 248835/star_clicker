package com.example.starclicker.dialogs.boosters

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.starclicker.R
import com.example.starclicker.ViewModelFactory
import com.example.starclicker.database.StarClickerDatabase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

class BoostersDialog private constructor(private val onExit : (() -> Unit)?) : DialogFragment(){
    private lateinit var viewModel: BoostersViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val datasource = StarClickerDatabase.getInstance(requireActivity().application).databaseDao
        val viewModelFactory = ViewModelFactory(datasource)

        viewModel = ViewModelProvider(this, viewModelFactory)[BoostersViewModel::class.java]

        val dialogContentView = layoutInflater.inflate(R.layout.boosters_dialog, null)

        val adapter = BoosterAdapter {
            Timber.e("Selected booster ID $it")
            dismiss()
        }
        dialogContentView.findViewById<RecyclerView>(R.id.boosts_recycler_view).adapter = adapter

        viewModel.boosters.observe(this, { list ->
            Timber.e("$list")
            adapter.submitList(list)
        })



        return MaterialAlertDialogBuilder(requireContext())
            .setView(dialogContentView)
            .setBackground(AppCompatResources.getDrawable(context!!, R.drawable.boosters_dialog_bg))
//            .setPositiveButton("OK") { _, _ -> onExit?.invoke() }
            .setOnDismissListener { onExit?.invoke() }
            .create()
    }

    companion object {
        fun newInstance(onExit: (() -> Unit)? = null): BoostersDialog {
            return BoostersDialog(onExit)
        }
    }
}

