package com.example.starclicker.rankingRv

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.starclicker.R

internal class AdapterUser(private val context: Context, private val users: MutableList<ModelUser?>) :
    RecyclerView.Adapter<AdapterUser.MyHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.one_user, parent, false)
        return MyHolder(view)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.tvName.text = (users.size-position).toString()+". "+users[position]?.getName()
        holder.tvScore.text = users[position]?.getScore().toString()
    }

    override fun getItemCount(): Int {
        return users.size
    }

    internal class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val tvScore: TextView = itemView.findViewById(R.id.tvScore)
        internal val tvName: TextView = itemView.findViewById(R.id.tvName)
    }
}