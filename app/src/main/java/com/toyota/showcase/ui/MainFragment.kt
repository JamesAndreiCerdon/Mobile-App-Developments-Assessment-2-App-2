package com.toyota.showcase.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.toyota.showcase.R
import com.toyota.showcase.databinding.FragmentMainBinding
import com.toyota.showcase.model.ToyotaCar
import com.toyota.showcase.ui.adapters.CarAdapter
import com.toyota.showcase.ui.activities.CarDetailsActivity

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
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
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        carAdapter = CarAdapter(ToyotaCar.getSampleCars()) { car ->
            val intent = Intent(requireContext(), CarDetailsActivity::class.java).apply {
                putExtra(CarDetailsActivity.EXTRA_CAR, car)
            }
            startActivity(intent)
        }

        binding.carsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = carAdapter
            
            // Set up click listener for favorite buttons
            (adapter as CarAdapter).setOnFavoriteToggleListener { car, isFavorite ->
                val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                prefs.edit().putBoolean(car.name, isFavorite).apply()
            }
        }

        carAdapter.setOnFavoriteToggleListener { car, isFavorite ->
            val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            prefs.edit().putBoolean(car.name, isFavorite).apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 