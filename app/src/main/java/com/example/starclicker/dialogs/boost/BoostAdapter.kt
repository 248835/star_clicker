package com.example.starclicker.dialogs.boost

import android.service.autofill.TextValueSanitizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.starclicker.R
import com.example.starclicker.database.Booster

class BoostAdapter(private val dataSet : LiveData<List<Booster>>, private val infoDialog : View?) : RecyclerView.Adapter<BoostAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val priceTextView : TextView = view.findViewById(R.id.price)
        val nameTextView : TextView = view.findViewById(R.id.name)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.booster_row, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val booster = dataSet.value!![position]
        viewHolder.priceTextView.text = booster.price.toString()
        viewHolder.nameTextView.text = booster.name
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = if(dataSet.value != null) dataSet.value!!.size else 0
}