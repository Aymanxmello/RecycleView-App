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

    // Liste des animaux (mutable pour permettre la suppression)
    private val animalList: MutableList<Animal> = mutableListOf()

    // Définir le nombre de colonnes pour la vue Grille
    private val GRID_COLUMNS = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialiser les données (charger les animaux)
        loadAnimalData()

        // 2. Initialiser les vues
        recyclerView = findViewById(R.id.recycler_view_animaux)
        radioGroupLayout = findViewById(R.id.radio_group_layout)

        // 3. Configurer l'Adaptateur
        adapterAnimaux = AdapterAnimaux(this, animalList)
        recyclerView.adapter = adapterAnimaux

        // 4. Définir le Layout initial (Linéaire par défaut)
        setLinearLayout()

        // 5. Gérer le changement de Layout via RadioGroup
        radioGroupLayout.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_lineaire -> setLinearLayout()
                R.id.rb_grille -> setGridLayout()
            }
        }
    }

    // Fonction pour charger les données initiales
    private fun loadAnimalData() {
        // IMPORTANT: Assurez-vous que ces ressources d'image existent dans res/drawable/
        animalList.add(Animal("Chat", "Mammifère", R.drawable.chat))
        animalList.add(Animal("Perroquet", "Oiseau", R.drawable.perroquet))
        animalList.add(Animal("Tigre", "Mammifère", R.drawable.tigre))
        animalList.add(Animal("Éléphant", "Mammifère", R.drawable.elephant))
        animalList.add(Animal("Serpent", "Reptile", R.drawable.serpent))
    }

    // Définit l'affichage en liste verticale (Linéaire)
    private fun setLinearLayout() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        // L'adaptateur est notifié automatiquement lors de la création
    }

    // Définit l'affichage en grille (Grille)
    private fun setGridLayout() {
        recyclerView.layoutManager = GridLayoutManager(this, GRID_COLUMNS)
    }
}