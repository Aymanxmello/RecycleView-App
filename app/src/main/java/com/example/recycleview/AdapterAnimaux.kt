package com.example.recycleview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout // NOUVEL IMPORT
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.example.recycleview.R
import com.example.recycleview.Animal

// Interface to communicate selection changes back to the Activity
interface SelectionCallback {
    fun onSelectionChanged(selectedCount: Int)
}

class AdapterAnimaux(
    private val context: Context,
    private val dataSet: MutableList<Animal>, // La liste doit être mutable pour la suppression
    private val selectionCallback: SelectionCallback // Callback vers l'activité
) : RecyclerView.Adapter<AdapterAnimaux.ViewHolder>() {

    // Flag pour gérer le mode de sélection
    var isSelectionMode = false
        set(value) {
            field = value
            if (!value) {
                // Clear selection when exiting mode
                dataSet.forEach { it.isSelected = false }
            }
            notifyDataSetChanged() // Rebind all items to show/hide checkboxes/buttons/selection
        }

    // 1. ViewHolder: Contient les vues d'un élément
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.img_animal)
        val nomTextView: TextView = view.findViewById(R.id.tv_nom)
        val especeTextView: TextView = view.findViewById(R.id.tv_espece)
        val detailsButton: MaterialButton = view.findViewById(R.id.btn_details)
        val supprimerButton: MaterialButton = view.findViewById(R.id.btn_supprimer)
        val selectionCheckbox: CheckBox = view.findViewById(R.id.cb_selection) // Checkbox
        val cardContentView: ConstraintLayout = view.findViewById(R.id.card_content_layout) // Zone de clic
    }

    // 2. onCreateViewHolder: Crée la vue de l'élément à partir du layout XML
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    // 3. onBindViewHolder: Lie les données aux vues et définit les écouteurs d'événements
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal = dataSet[position]

        // Lie les données
        holder.nomTextView.text = animal.nom
        holder.especeTextView.text = animal.espece
        holder.imageView.setImageResource(animal.imageResId)

        // Visibilité conditionnelle basée sur le mode de sélection
        if (isSelectionMode) {
            holder.detailsButton.visibility = View.GONE
            holder.supprimerButton.visibility = View.GONE
            holder.selectionCheckbox.visibility = View.VISIBLE
            holder.selectionCheckbox.isChecked = animal.isSelected
        } else {
            holder.detailsButton.visibility = View.VISIBLE
            holder.supprimerButton.visibility = View.VISIBLE
            holder.selectionCheckbox.visibility = View.GONE
        }

        // --- Clicks and Long Clicks ---

        // Checkbox listener
        holder.selectionCheckbox.setOnClickListener {
            val isChecked = (it as CheckBox).isChecked
            animal.isSelected = isChecked
            selectionCallback.onSelectionChanged(dataSet.count { it.isSelected })
        }

        // Card content click (toggle selection or default action)
        holder.cardContentView.setOnClickListener {
            if (isSelectionMode) {
                // Toggle selection state
                animal.isSelected = !animal.isSelected
                holder.selectionCheckbox.isChecked = animal.isSelected
                selectionCallback.onSelectionChanged(dataSet.count { it.isSelected })
            } else {
                // Default action
                showDetailsDialog(animal)
            }
        }

        // Long press to activate selection mode and select the item
        holder.cardContentView.setOnLongClickListener {
            if (!isSelectionMode) {
                animal.isSelected = true // Select the long-pressed item
                selectionCallback.onSelectionChanged(1) // This triggers selection mode in MainActivity
                true // Consume the long click
            } else {
                false
            }
        }

        // Single Delete Button Listener (only active in non-selection mode)
        if (!isSelectionMode) {
            holder.detailsButton.setOnClickListener {
                showDetailsDialog(animal)
            }

            holder.supprimerButton.setOnClickListener {
                val currentPosition = holder.bindingAdapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    showDeleteConfirmationDialog(currentPosition)
                }
            }
        }
    }

    // 4. getItemCount: Retourne le nombre total d'éléments
    override fun getItemCount(): Int = dataSet.size

    // Supprime l'élément unique de la liste (utilisé hors mode sélection)
    private fun removeItem(position: Int) {
        val animalName = dataSet[position].nom
        dataSet.removeAt(position)
        notifyItemRemoved(position)
        Toast.makeText(context, "$animalName a été supprimé.", Toast.LENGTH_SHORT).show()
    }

    // Affiche la boîte de dialogue de confirmation de suppression (unique)
    private fun showDeleteConfirmationDialog(position: Int) {
        val animalName = dataSet[position].nom

        AlertDialog.Builder(context)
            .setTitle("Confirmer la suppression")
            .setMessage("Voulez-vous vraiment supprimer $animalName de la liste ?")
            .setPositiveButton("Oui") { dialog, which ->
                removeItem(position)
            }
            .setNegativeButton("Non", null)
            .show()
    }

    // Affiche les détails de l'animal dans une boîte de dialogue
    private fun showDetailsDialog(animal: Animal) {
        val message = "Nom: ${animal.nom}\nEspèce: ${animal.espece}"
        AlertDialog.Builder(context)
            .setTitle("Détails de l'Animal")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    // Fonction pour supprimer tous les éléments sélectionnés
    fun deleteSelectedItems() {
        val selectedItems = dataSet.filter { it.isSelected }.toSet()
        val deletedCount = selectedItems.size

        dataSet.removeAll(selectedItems)

        // Quitte le mode sélection, ce qui appelle notifyDataSetChanged() via le setter
        isSelectionMode = false

        Toast.makeText(context, "$deletedCount élément(s) supprimé(s).", Toast.LENGTH_SHORT).show()
    }
}