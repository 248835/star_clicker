package com.example.starclicker.dialogs.boosters.info

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.starclicker.R
import com.example.starclicker.ViewModelFactory
import com.example.starclicker.database.StarClickerDatabase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

class BoostersInfoDialog private constructor() : DialogFragment(){
    private lateinit var viewModel: BoostersInfoViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val datasource = StarClickerDatabase.getInstance(requireActivity().application).databaseDao
        val viewModelFactory = ViewModelFactory(datasource)

        viewModel = ViewModelProvider(this, viewModelFactory)[BoostersInfoViewModel::class.java]

        val dialogContentView = layoutInflater.inflate(R.layout.boosters_info_dialog, null)
        val recyclerView = dialogContentView.findViewById<RecyclerView>(R.id.boosts_recycler_view)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.boosters.observe(this, { list ->
            // this may fire up twice. Make sure it's not a problem
            recyclerView.apply{
                adapter = BoosterInfoAdapter(viewModel.boosters, null)
            }
            Timber.e("$list")
            // adapter.submitList(list)
        })



        return MaterialAlertDialogBuilder(requireContext())
            .setView(dialogContentView)
            .setBackground(AppCompatResources.getDrawable(context!!, R.drawable.boosters_dialog_bg))
            .setPositiveButton("OK") { dialog, which ->
                // Respond to positive button press
            }
            .create()
    }

    companion object {
        fun newInstance(): BoostersInfoDialog {
            return BoostersInfoDialog()
        }
    }
}