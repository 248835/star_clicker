package com.example.starclicker.instructions.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InfoDialog private constructor() : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Instrukcja")
            .setMessage("Gra polega na zdobywaniu punktów poprzez klikanie gwiazdek." +
                        " Są 2 typy gwiazdek: zwykłe - dają punkty, specjalne - uruchamiają tryb trzęsienia." +
                        " W trybie trzęsienia należy trząść telefonem, aby zdobywać punkty." +
                        " W trakcie gry regularnie liczba punktów spada. Jeśli spadnie do 0 gra się kończy." +
                        " W trakcie gry za zdobyte punkty można kupić boostery, które ułatwiają rozgrywkę.")
            .setNeutralButton("ok",null)
            .create()
    }

    companion object {
        fun newInstance(): InfoDialog {
            return InfoDialog()
        }
    }
}