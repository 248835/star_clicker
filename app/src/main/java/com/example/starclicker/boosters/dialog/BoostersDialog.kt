package com.example.starclicker.boosters.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.starclicker.R
import com.example.starclicker.boosters.Boosters
import com.example.starclicker.game.GameViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber


class BoostersDialog private constructor(private val onExit: (() -> Unit)?) : DialogFragment() {
    private lateinit var viewModel: BoostersViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        viewModel = ViewModelProvider(this)[BoostersViewModel::class.java]

        val dialogContentView = layoutInflater.inflate(R.layout.boosters_dialog, null).apply {
            recyclerView = findViewById<RecyclerView>(R.id.boosts_recycler_view)
            recyclerView.adapter = BoosterAdapter {
                Timber.e("Selected booster ID $it")
                it.active.value = true
                val gameViewModel: GameViewModel by viewModels(
                    ownerProducer = { requireParentFragment() }
                )
                gameViewModel.deactivateAfterDelay(it)
                dismiss()
            }.apply {
                submitList(Boosters.boosters.sortedBy { it.price })
            }

            Boosters.boosters.forEach {
                it.active.observe(this@BoostersDialog, {recyclerView.adapter!!.notifyDataSetChanged()})
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

