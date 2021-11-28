package com.example.starclicker.dialogs.boosters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.starclicker.database.Booster
import com.example.starclicker.databinding.BoosterItemBinding

class BoosterAdapter(private val listener: (Long) -> Unit) :
    ListAdapter<Booster, BoosterAdapter.ViewHolder>(BoosterDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(
        private val binding: BoosterItemBinding,
        private val listener: (Long) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Booster) {
            binding.root.setOnClickListener{
                listener.invoke(item.id)
            }
            binding.booster = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup, listener: (Long) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BoosterItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, listener)
            }
        }
    }

    private class BoosterDiffCallback : DiffUtil.ItemCallback<Booster>() {
        override fun areItemsTheSame(oldItem: Booster, newItem: Booster): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Booster, newItem: Booster): Boolean {
            return oldItem == newItem
        }

    }
}