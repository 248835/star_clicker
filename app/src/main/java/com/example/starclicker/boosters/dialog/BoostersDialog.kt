package com.example.starclicker.boosters.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.starclicker.R
import com.example.starclicker.boosters.Boosters
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber


class BoostersDialog private constructor(private val onExit: (() -> Unit)?) : DialogFragment() {
    private lateinit var viewModel: BoostersViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        viewModel = ViewModelProvider(this)[BoostersViewModel::class.java]

        val dialogContentView = layoutInflater.inflate(R.layout.boosters_dialog, null).apply {
            findViewById<RecyclerView>(R.id.boosts_recycler_view).adapter = BoosterAdapter {
                Timber.e("Selected booster ID $it")
                it.active.value = true
                dismiss()
            }.apply {
                submitList(Boosters.boosters.sortedBy { it.price })
            }
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setView(dialogContentView)
            .setTitle(R.string.shop)
            //.setBackground(AppCompatResources.getDrawable(context!!, R.drawable.boosters_dialog_bg))
            .setPositiveButton("OK") { _, _ -> onExit?.invoke() }
            .setOnDismissListener { onExit?.invoke() }
            .create()
    }

    override fun onDestroy() {
        onExit?.invoke()
        super.onDestroy()
    }

    companion object {
        fun newInstance(onExit: (() -> Unit)? = null): BoostersDialog {
            return BoostersDialog(onExit)
        }
    }
}

