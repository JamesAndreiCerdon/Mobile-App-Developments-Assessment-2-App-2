package com.toyota.showcase.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.toyota.showcase.R
import com.toyota.showcase.model.ToyotaCar

class SearchResultAdapter(private val onCarClick: (ToyotaCar) -> Unit) : 
    RecyclerView.Adapter<SearchResultAdapter.SearchViewHolder>() {

    private var cars: List<ToyotaCar> = emptyList()

    fun updateResults(newCars: List<ToyotaCar>) {
        cars = newCars
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val car = cars[position]
        holder.bind(car)
    }

    override fun getItemCount(): Int = cars.size

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.carNameTextView)
        private val detailsTextView: TextView = itemView.findViewById(R.id.carDetailsTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.carPriceTextView)

        fun bind(car: ToyotaCar) {
            nameTextView.text = car.name
            detailsTextView.text = "${car.engine} • ${car.transmission} • ${car.driveType}"
            priceTextView.text = car.price
            
            itemView.setOnClickListener { onCarClick(car) }
        }
    }
} 