package com.example.starclicker.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BoostDialog private constructor() : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
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

