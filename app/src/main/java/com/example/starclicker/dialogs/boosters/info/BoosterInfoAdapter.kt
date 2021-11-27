package com.example.starclicker.dialogs.boosters.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.starclicker.R
import com.example.starclicker.database.Booster

class BoosterInfoAdapter(private val dataSet : LiveData<List<Booster>>, private val infoDialog : View?) : RecyclerView.Adapter<BoosterInfoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView : TextView = view.findViewById(R.id.name)
        val descTextView : TextView = view.findViewById(R.id.description)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.booster_info_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val booster = dataSet.value!![position]
        viewHolder.nameTextView.text = booster.name
        viewHolder.descTextView.text = booster.description
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = if(dataSet.value != null) dataSet.value!!.size else 0
}