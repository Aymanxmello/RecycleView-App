package com.example.recycleview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout // NOUVEL IMPORT
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.button.MaterialButton


// Implement the new SelectionCallback interface
class MainActivity : AppCompatActivity(), SelectionCallback {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterAnimaux: AdapterAnimaux
    private lateinit var radioGroupLayout: RadioGroup

    // UI elements for selection mode
    private lateinit var tvTitle: TextView
    private lateinit var tvSelectionCount: TextView
    private lateinit var btnDeleteSelected: MaterialButton

    // List of animals (mutable to allow deletion)
    private val animalList: MutableList<Animal> = mutableListOf()

    // Define the number of columns for the Grid view
    private val GRID_COLUMNS = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialize data (load animals)
        loadAnimalData()

        // 2. Initialize views
        recyclerView = findViewById(R.id.recycler_view_animaux)
        radioGroupLayout = findViewById(R.id.radio_group_layout)

        // Initialize new views
        tvTitle = findViewById(R.id.tv_title)
        tvSelectionCount = findViewById(R.id.tv_selection_count)
        btnDeleteSelected = findViewById(R.id.btn_delete_selected)


        // 3. Configure the Adapter, passing 'this' as the SelectionCallback
        adapterAnimaux = AdapterAnimaux(this, animalList, this)
        recyclerView.adapter = adapterAnimaux

        // 4. Set the initial Layout (Linear by default)
        setLinearLayout()

        // 5. Manage Layout change via RadioGroup
        radioGroupLayout.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_lineaire -> setLinearLayout()
                R.id.rb_grille -> setGridLayout()
            }
        }

        // Setup click listener for the multi-delete button
        btnDeleteSelected.setOnClickListener {
            adapterAnimaux.deleteSelectedItems()
            // Manually update the toolbar to reset to default mode
            updateToolbarAndConstraints(0)
        }

        // Initial setup for constraints and UI (Default mode: selectedCount = 0)
        updateToolbarAndConstraints(0)
    }

    // Function to load initial data
    private fun loadAnimalData() {
        animalList.add(Animal("Chat", "Mammifère", R.drawable.chat))
        animalList.add(Animal("Perroquet", "Oiseau", R.drawable.perroquet))
        animalList.add(Animal("Tigre", "Mammifère", R.drawable.tigre))
        animalList.add(Animal("Éléphant", "Mammifère", R.drawable.elephant))
        animalList.add(Animal("Serpent", "Reptile", R.drawable.serpent))
    }

    // Sets the vertical list display (Linéaire)
    private fun setLinearLayout() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    // Sets the grid display (Grille)
    private fun setGridLayout() {
        recyclerView.layoutManager = GridLayoutManager(this, GRID_COLUMNS)
    }

    // --- Implementation of SelectionCallback interface ---
    override fun onSelectionChanged(selectedCount: Int) {

        // 1. Synchronize the adapter's selection mode based on the count
        val shouldBeInSelectionMode = selectedCount > 0
        if (adapterAnimaux.isSelectionMode != shouldBeInSelectionMode) {
            // This sets adapterAnimaux.isSelectionMode and calls notifyDataSetChanged() in the adapter
            adapterAnimaux.isSelectionMode = shouldBeInSelectionMode
        }

        // 2. Update the toolbar and constraints
        updateToolbarAndConstraints(selectedCount)
    }

    // Function to update the UI (title, count, button visibility) and RecyclerView constraint
    private fun updateToolbarAndConstraints(selectedCount: Int) {
        if (selectedCount > 0) {
            // Selection mode is ON: Show selection UI and hide default UI
            tvTitle.visibility = View.GONE
            tvSelectionCount.visibility = View.VISIBLE
            btnDeleteSelected.visibility = View.VISIBLE
            radioGroupLayout.visibility = View.GONE

            // Update count text and button text
            tvSelectionCount.text = "$selectedCount sélectionné(s)"
            btnDeleteSelected.text = "Supprimer ($selectedCount)"

            // FIX: Constrain RecyclerView to the bottom of the selection count TextView
            setRecyclerViewConstraint(R.id.tv_selection_count)

        } else {
            // Selection mode is OFF: Show default UI and hide selection UI
            tvTitle.visibility = View.VISIBLE
            tvSelectionCount.visibility = View.GONE
            btnDeleteSelected.visibility = View.GONE
            radioGroupLayout.visibility = View.VISIBLE

            // FIX: Constrain RecyclerView to the bottom of the RadioGroup
            setRecyclerViewConstraint(R.id.radio_group_layout)
        }
    }

    // Helper function to set the RecyclerView's top constraint programmatically
    private fun setRecyclerViewConstraint(anchorId: Int) {
        // We use view.layoutParams as ConstraintLayout.LayoutParams to access topToBottom property
        val layoutParams = recyclerView.layoutParams as ConstraintLayout.LayoutParams

        // Set the new constraint to the bottom of the anchor view
        layoutParams.topToBottom = anchorId

        // Apply changes
        recyclerView.layoutParams = layoutParams
    }
}