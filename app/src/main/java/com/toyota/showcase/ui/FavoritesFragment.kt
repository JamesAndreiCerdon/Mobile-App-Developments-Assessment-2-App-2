package com.toyota.showcase.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.toyota.showcase.data.PreferencesManager
import com.toyota.showcase.databinding.ActivityFavoritesBinding
import com.toyota.showcase.model.ToyotaCar
import com.toyota.showcase.ui.adapters.CarAdapter
import com.toyota.showcase.ui.activities.CarDetailsActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {
    private var _binding: ActivityFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var carAdapter: CarAdapter
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityFavoritesBinding.inflate(inflater, container, false)
        preferencesManager = PreferencesManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch {
            val favoriteCarNames = preferencesManager.getAllFavorites().first()
            val favoriteCars = ToyotaCar.getSampleCars().filter { car ->
                favoriteCarNames.contains(car.name)
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
                viewLifecycleOwner.lifecycleScope.launch {
                    preferencesManager.setFavorite(car.name, isFavorite)
                    if (!isFavorite) {
                        setupRecyclerView()
                        updateEmptyState()
                    }
                }
            }

            // Set initial favorite states
            favoriteCars.forEach { car ->
                carAdapter.updateFavoriteState(car.name, true)
            }

            updateEmptyState()
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