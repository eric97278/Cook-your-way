package fr.eric97278.cookyourway.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import fr.eric97278.cookyourway.R

class RecipeAdapter(private val layoutId: Int) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>(){

    //boite pour ranger tout les composant a controller
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val recipeImage = view.findViewById<ImageView>(R.id.image_item)

    }
        //Injecte le composant
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

            return ViewHolder(view)
    }
        //combien d'item dans la liste
    override fun getItemCount(): Int = 5

        //mets a jour chaque composant
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }
}