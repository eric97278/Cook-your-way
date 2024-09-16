package fr.eric97278.cookyourway.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.eric97278.cookyourway.MainActivity
import fr.eric97278.cookyourway.R
import fr.eric97278.cookyourway.RecipeModel

class RecipeAdapter(
    private val context: MainActivity,
    private val recipeList: List<RecipeModel>,
    private val layoutId: Int) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    //boite pour ranger tout les composant a controller
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeImage = view.findViewById<ImageView>(R.id.image_item)
        val recipeName: TextView? = view.findViewById(R.id.name_item)
        val recipeDescription: TextView? = view.findViewById(R.id.description_item)
        val recipeDifficultySubtitle: TextView? = view.findViewById(R.id.difficulty_subtitle_item)
        val recipeTimeSubtitle: TextView? = view.findViewById(R.id.time_subtitle_item)
        val recipeId:TextView? = view.findViewById(R.id.id_item)
        val recipeIngredients:TextView? = view.findViewById(R.id.ingredients_item)
        val recipeSteps:TextView? = view.findViewById(R.id.steps_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)


    }

    //Injecte le composant
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    //combien d'item dans la liste
    override fun getItemCount(): Int = recipeList.size

    //mets a jour chaque composant
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // recuperer les informations de la recette
        val currentRecipe = recipeList[position]

        //mettre a jour le nom de la recette
        holder.recipeName?.text = currentRecipe.name

        //mettre a jour la description de la recette
        holder.recipeDescription?.text = currentRecipe.description

        //mettre a jour la difficulté de la recette
        holder.recipeDifficultySubtitle?.text = currentRecipe.difficulty

        //mettre a jour le temps de la recette
        holder.recipeTimeSubtitle?.text = currentRecipe.time.toString()

        //utiliser glide pour récupérer l'image à partir de son lien -> composant
        Glide.with(context).load(Uri.parse(currentRecipe.imageUrl)).into(holder.recipeImage)

        //mettre a jour les ingredients de la recette
        holder.recipeIngredients?.text = currentRecipe.ingredients

        //mettre a jour les steps de la recette
        holder.recipeSteps?.text = currentRecipe.steps

        //mettre a jour l'id de la recette
        holder.recipeId?.text = currentRecipe.id


        //vérifier si la recette est liké
        if (currentRecipe.liked) {
            holder.starIcon.setImageResource(R.drawable.ic_star)
        } else {
            holder.starIcon.setImageResource(R.drawable.ic_unstar)
        }
    }
}