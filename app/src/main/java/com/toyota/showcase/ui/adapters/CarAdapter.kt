package com.toyota.showcase.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.toyota.showcase.R
import com.toyota.showcase.model.ToyotaCar

class CarAdapter(
    private val cars: List<ToyotaCar>,
    private val onCarClick: (ToyotaCar) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    private var onFavoriteToggle: ((ToyotaCar, Boolean) -> Unit)? = null
    private val favoriteStates = mutableMapOf<String, Boolean>()

    fun setOnFavoriteToggleListener(listener: (ToyotaCar, Boolean) -> Unit) {
        onFavoriteToggle = listener
    }

    fun updateFavoriteState(carName: String, isFavorite: Boolean) {
        favoriteStates[carName] = isFavorite
        notifyItemChanged(cars.indexOfFirst { it.name == carName })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        holder.bind(cars[position])
    }

    override fun getItemCount(): Int = cars.size

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val carImage: ImageView = itemView.findViewById(R.id.carImage)
        private val carName: TextView = itemView.findViewById(R.id.carName)
        private val carPrice: TextView = itemView.findViewById(R.id.carPrice)
        private val favoriteToggle: ToggleButton = itemView.findViewById(R.id.favoriteToggle)

        fun bind(car: ToyotaCar) {
            carImage.setImageResource(car.image)
            carName.text = car.name
            carPrice.text = car.price
            itemView.setOnClickListener { onCarClick(car) }
            
            // Set initial favorite state without triggering the listener
            favoriteToggle.setOnCheckedChangeListener(null)
            favoriteToggle.isChecked = favoriteStates[car.name] ?: false
            favoriteToggle.setOnCheckedChangeListener { _, isChecked ->
                onFavoriteToggle?.invoke(car, isChecked)
            }
        }
    }
}