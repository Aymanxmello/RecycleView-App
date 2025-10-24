package com.example.recycleview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recycleview.Animal
import com.example.recycleview.AdapterAnimaux


import android.widget.RadioGroup

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapterAnimaux: AdapterAnimaux
    private lateinit var radioGroupLayout: RadioGroup

    // Mutable list to hold the data
    private val animalList: MutableList<Animal> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialize data
        loadAnimalData()

        // 2. Initialize Views
        recyclerView = findViewById(R.id.recycler_view_animaux)
        radioGroupLayout = findViewById(R.id.radio_group_layout)

        // 3. Setup Adapter
        adapterAnimaux = AdapterAnimaux(this, animalList)
        recyclerView.adapter = adapterAnimaux

        // 4. Initial Layout: Linear (by default)
        setLinearLayout()

        // 5. Handle Layout Switching
        radioGroupLayout.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_lineaire -> setLinearLayout()
                R.id.rb_grille -> setGridLayout()
            }
        }
    }

    // Function to load initial data (ensure you have these drawables!)
    private fun loadAnimalData() {
        // NOTE: Replace R.drawable.xxx with your actual drawable resource IDs.
        animalList.add(Animal("Chat", "Mammifère", R.drawable.chat))
        animalList.add(Animal("Perroquet", "Oiseau", R.drawable.perroquet))
        animalList.add(Animal("Tigre", "Mammifère", R.drawable.tigre))
        animalList.add(Animal("Éléphant", "Mammifère", R.drawable.elephant))
        animalList.add(Animal("Serpent", "Reptile", R.drawable.serpent))
        // Add more animals as needed...
    }

    // Set layout to Linear (vertical list)
    private fun setLinearLayout() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Refresh the adapter in case data was modified in the grid view
        adapterAnimaux.notifyDataSetChanged()
    }

    // Set layout to Grid (2 columns)
    private fun setGridLayout() {
        // Using 2 columns for the grid
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        // Refresh the adapter
        adapterAnimaux.notifyDataSetChanged()
    }
}