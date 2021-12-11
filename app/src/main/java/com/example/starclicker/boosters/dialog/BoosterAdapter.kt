package com.example.starclicker.boosters.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Transformations
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.starclicker.R
import com.example.starclicker.boosters.Booster
import com.example.starclicker.databinding.BoosterItemBinding
import com.google.android.material.color.MaterialColors
import timber.log.Timber

class BoosterAdapter(private val listener: (Booster) -> Unit) :
    ListAdapter<Booster, BoosterAdapter.ViewHolder>(BoosterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(
        private val binding: BoosterItemBinding,
        private val listener: (Booster) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Booster) {
            binding.root.setOnClickListener{
                listener.invoke(item)
            }
            binding.booster = item

            /*Transformations.map(item.active) {
                if (it == false)
                    binding.root.setBackgroundColor(MaterialColors.getColor(binding.root,R.attr.colorOnPrimary))
            }*/
            if (item.active.value == true)
                binding.root.setBackgroundColor(MaterialColors.getColor(binding.root,R.attr.colorAccent))
            else
                binding.root.setBackgroundColor(MaterialColors.getColor(binding.root,R.attr.colorOnPrimary))

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, listener: (Booster) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BoosterItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, listener)
            }
        }
    }

    private class BoosterDiffCallback : DiffUtil.ItemCallback<Booster>() {
        override fun areItemsTheSame(oldItem: Booster, newItem: Booster): Boolean {
            return oldItem.displayedName == newItem.displayedName
        }

        override fun areContentsTheSame(oldItem: Booster, newItem: Booster): Boolean {
            return oldItem == newItem
        }

    }
}