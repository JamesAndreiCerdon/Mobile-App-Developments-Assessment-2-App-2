package com.toyota.showcase.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.toyota.showcase.databinding.ActivityFavoritesBinding
import com.toyota.showcase.model.ToyotaCar
import com.toyota.showcase.ui.adapters.CarAdapter
import com.toyota.showcase.ui.activities.CarDetailsActivity

class FavoritesFragment : Fragment() {
    private var _binding: ActivityFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var carAdapter: CarAdapter

    companion object {
        private const val PREFS_NAME = "favorites_prefs"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        updateEmptyState()
    }

    private fun setupRecyclerView() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val favoriteCars = ToyotaCar.getSampleCars().filter { car ->
            prefs.getBoolean(car.name, false)
        }

        carAdapter = CarAdapter(favoriteCars) { car ->
            val intent = Intent(requireContext(), CarDetailsActivity::class.java).apply {
                putExtra(CarDetailsActivity.EXTRA_CAR, car)
            }
            startActivity(intent)
        }

        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = carAdapter
        }

        carAdapter.setOnFavoriteToggleListener { car, isFavorite ->
            val editor = prefs.edit()
            editor.putBoolean(car.name, isFavorite).apply()
            
            // Refresh the list if a car is unfavorited
            if (!isFavorite) {
                setupRecyclerView()
                updateEmptyState()
            }
        }
    }

    private fun updateEmptyState() {
        binding.emptyFavoritesText.visibility = 
            if (carAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 