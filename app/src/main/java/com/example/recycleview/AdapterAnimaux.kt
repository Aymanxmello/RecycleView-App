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
import com.example.recycleview.R
import com.example.recycleview.Animal

class AdapterAnimaux(
    private val context: Context,
    private val dataSet: MutableList<Animal> // La liste doit être mutable pour la suppression
) : RecyclerView.Adapter<AdapterAnimaux.ViewHolder>() {

    // 1. ViewHolder: Contient les vues d'un élément
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.img_animal)
        val nomTextView: TextView = view.findViewById(R.id.tv_nom)
        val especeTextView: TextView = view.findViewById(R.id.tv_espece)
        val detailsButton: MaterialButton = view.findViewById(R.id.btn_details)
        val supprimerButton: MaterialButton = view.findViewById(R.id.btn_supprimer)
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

        // Bouton Détails : Affiche les informations de l'animal [cite: 9, 32]
        holder.detailsButton.setOnClickListener {
            showDetailsDialog(animal)
        }

        // Bouton Supprimer : Affiche la boîte de dialogue de confirmation [cite: 10, 33]
        holder.supprimerButton.setOnClickListener {
            val currentPosition = holder.bindingAdapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                showDeleteConfirmationDialog(currentPosition)
            }
        }
    }

    // 4. getItemCount: Retourne le nombre total d'éléments
    override fun getItemCount(): Int = dataSet.size

    // --- Fonctions de l'Adaptateur ---

    // Affiche la boîte de dialogue de confirmation de suppression
    private fun showDeleteConfirmationDialog(position: Int) {
        val animalName = dataSet[position].nom

        AlertDialog.Builder(context)
            .setTitle("Confirmer la suppression")
            .setMessage("Voulez-vous vraiment supprimer $animalName de la liste ?")
            .setPositiveButton("Oui") { dialog, which ->
                removeItem(position) // Supprime si l'utilisateur confirme
            }
            .setNegativeButton("Non", null) // Ne fait rien si l'utilisateur annule
            .show()
    }

    // Supprime l'élément de la liste et notifie l'adaptateur
    private fun removeItem(position: Int) {
        val animalName = dataSet[position].nom
        dataSet.removeAt(position)
        notifyItemRemoved(position)
        Toast.makeText(context, "$animalName a été supprimé.", Toast.LENGTH_SHORT).show()
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
}