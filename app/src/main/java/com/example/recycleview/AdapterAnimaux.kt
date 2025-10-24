package com.example.recycleview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton


class AdapterAnimaux(private val context: Context, private val dataSet: MutableList<Animal>) :
    RecyclerView.Adapter<AdapterAnimaux.ViewHolder>() {

    // 1. ViewHolder: Holds the views for a single item
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.img_animal)
        val nomTextView: TextView = view.findViewById(R.id.tv_nom)
        val especeTextView: TextView = view.findViewById(R.id.tv_espece)
        val detailsButton: MaterialButton = view.findViewById(R.id.btn_details)
        val supprimerButton: MaterialButton = view.findViewById(R.id.btn_supprimer)
    }

    // 2. onCreateViewHolder: Inflates the item layout (item_animal.xml)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    // 3. onBindViewHolder: Binds data to the views and sets up click listeners
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal = dataSet[position]

        // Bind data
        holder.nomTextView.text = animal.nom
        holder.especeTextView.text = animal.espece
        holder.imageView.setImageResource(animal.imageResId) // Set the image

        // Details Button Click Listener
        holder.detailsButton.setOnClickListener {
            showDetailsDialog(animal)
        }

        // Delete Button Click Listener
        holder.supprimerButton.setOnClickListener {
            // Get the current position (important for correct deletion)
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                removeItem(currentPosition)
            }
        }
    }

    // 4. getItemCount: Returns the total number of items
    override fun getItemCount(): Int = dataSet.size

    // --- Custom Functions ---

    // Function to handle item deletion
    private fun removeItem(position: Int) {
        val animalName = dataSet[position].nom
        dataSet.removeAt(position)
        notifyItemRemoved(position)
        Toast.makeText(context, "$animalName a été supprimé de la liste", Toast.LENGTH_SHORT).show()
    }

    // Function to display animal details
    private fun showDetailsDialog(animal: Animal) {
        val message = "Nom: ${animal.nom}\nEspèce: ${animal.espece}"
        AlertDialog.Builder(context)
            .setTitle("Détails de l'Animal")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    // Function to update the data set (if needed for list/grid switch)
    fun updateData(newData: List<Animal>) {
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }
}