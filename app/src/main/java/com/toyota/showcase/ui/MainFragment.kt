package com.toyota.showcase.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.toyota.showcase.R
import com.toyota.showcase.data.PreferencesManager
import com.toyota.showcase.databinding.FragmentMainBinding
import com.toyota.showcase.model.ToyotaCar
import com.toyota.showcase.ui.adapters.CarAdapter
import com.toyota.showcase.ui.activities.CarDetailsActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var carAdapter: CarAdapter
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        preferencesManager = PreferencesManager(requireContext())
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
        }

        carAdapter.setOnFavoriteToggleListener { car, isFavorite ->
            viewLifecycleOwner.lifecycleScope.launch {
                preferencesManager.setFavorite(car.name, isFavorite)
            }
        }

        // Load initial favorite states
        viewLifecycleOwner.lifecycleScope.launch {
            ToyotaCar.getSampleCars().forEach { car ->
                val isFavorite = preferencesManager.isFavorite(car.name).first()
                carAdapter.updateFavoriteState(car.name, isFavorite)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 