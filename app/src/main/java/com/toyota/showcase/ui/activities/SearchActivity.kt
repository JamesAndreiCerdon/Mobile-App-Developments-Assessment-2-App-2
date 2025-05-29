package com.toyota.showcase.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.toyota.showcase.R
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.toyota.showcase.model.ToyotaCar
import com.toyota.showcase.ui.adapters.SearchResultAdapter

class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var backButton: ImageButton
    private lateinit var clearButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noResultsTextView: TextView
    private lateinit var searchAdapter: SearchResultAdapter
    private val allCars = ToyotaCar.getSampleCars()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initializeViews()
        setupListeners()
        setupRecyclerView()
    }

    private fun initializeViews() {
        searchEditText = findViewById(R.id.searchEditText)
        backButton = findViewById(R.id.backButton)
        clearButton = findViewById(R.id.clearButton)
        recyclerView = findViewById(R.id.searchResultsRecyclerView)
        progressBar = findViewById(R.id.searchProgressBar)
        noResultsTextView = findViewById(R.id.noResultsTextView)
    }

    private fun setupListeners() {
        backButton.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchEditText.text.clear()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = if (s?.isNotEmpty() == true) View.VISIBLE else View.GONE
                performSearch(s?.toString() ?: "")
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(searchEditText.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchResultAdapter { car ->
            // Handle car click - navigate to details
            val intent = Intent(this, CarDetailsActivity::class.java).apply {
                putExtra(CarDetailsActivity.EXTRA_CAR, car)
            }
            startActivity(intent)
        }
        
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }
    }

    private fun performSearch(query: String) {
        if (query.isEmpty()) {
            showNoResults(false)
            searchAdapter.updateResults(emptyList())
            return
        }

        showLoading(true)
        
        // Search implementation
        val results = allCars.filter { car ->
            car.name.contains(query, ignoreCase = true) ||
            car.engine.contains(query, ignoreCase = true) ||
            car.price.contains(query, ignoreCase = true) ||
            car.transmission.contains(query, ignoreCase = true) ||
            car.driveType.contains(query, ignoreCase = true)
        }

        // Update UI with results
        showLoading(false)
        if (results.isEmpty()) {
            showNoResults(true)
        } else {
            showNoResults(false)
            searchAdapter.updateResults(results)
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
        noResultsTextView.visibility = View.GONE
    }

    private fun showNoResults(show: Boolean) {
        noResultsTextView.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }
} 