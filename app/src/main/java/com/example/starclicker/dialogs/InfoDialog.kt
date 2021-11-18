package com.example.starclicker.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InfoDialog private constructor() : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Instrukcja")
            .setMessage("Tutaj powinna być instrukcja gry")
            .setNeutralButton("ok",null)
            .create()
    }

    companion object {
        fun newInstance(): InfoDialog {
            return InfoDialog()
        }
    }
}