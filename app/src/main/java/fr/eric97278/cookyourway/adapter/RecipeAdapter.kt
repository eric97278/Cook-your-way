package fr.eric97278.cookyourway.adapter

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
import android.util.Log
import fr.eric97278.cookyourway.RecipePopup
import fr.eric97278.cookyourway.RecipeRepository

class RecipeAdapter(
    val context: MainActivity,
    private val recipeList: List<RecipeModel>,
    private val layoutId: Int
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    // Boite pour ranger tous les composants à contrôler
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeImage = view.findViewById<ImageView>(R.id.image_item)
        val recipeName: TextView? = view.findViewById(R.id.name_item)
        val recipeDescription: TextView? = view.findViewById(R.id.description_item)
        val recipeDifficultySubtitle: TextView? = view.findViewById(R.id.difficulty_subtitle_item)
        val recipeTimeSubtitle: TextView? = view.findViewById(R.id.time_subtitle_item)
        val recipeId: TextView? = view.findViewById(R.id.id_item)
        val recipeIngredients: TextView? = view.findViewById(R.id.ingredients_item)
        val recipeSteps: TextView? = view.findViewById(R.id.steps_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)
    }

    // Injecte le composant
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }

    // Nombre d'items dans la liste
    override fun getItemCount(): Int = recipeList.size

    // Mets à jour chaque composant
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Récupérer les informations de la recette
        val currentRecipe = recipeList[position]

        // Récupérer le repository
        val repo = RecipeRepository()

        // Mettre à jour le nom de la recette
        holder.recipeName?.text = currentRecipe.name

        // Mettre à jour la description de la recette
        holder.recipeDescription?.text = currentRecipe.description

        // Mettre à jour la difficulté de la recette
        holder.recipeDifficultySubtitle?.text = currentRecipe.difficulty.toString()

        // Mettre à jour le temps de la recette
        holder.recipeTimeSubtitle?.text = currentRecipe.time.toString()

        // Déboguer l'URL de l'image
        Log.d("ImageURL", "URL de l'image: ${currentRecipe.imageUrl}")

        // Utiliser Glide pour récupérer l'image à partir de son lien
        Glide.with(context)
            .load(currentRecipe.imageUrl) // Pas besoin de Uri.parse() si c'est une chaîne
            .placeholder(R.drawable.ic_image_upload) // Image par défaut pendant le chargement
            .error(R.drawable.ic_image_broken) // Image en cas d'erreur
            .into(holder.recipeImage)

        // Mettre à jour les ingrédients de la recette
        holder.recipeIngredients?.text = currentRecipe.ingredients

        // Mettre à jour les étapes de la recette
        holder.recipeSteps?.text = currentRecipe.steps

        // Mettre à jour l'ID de la recette
        holder.recipeId?.text = currentRecipe.id

        // Vérifier si la recette est likée
        if (currentRecipe.liked) {
            holder.starIcon.setImageResource(R.drawable.ic_star)
        } else {
            holder.starIcon.setImageResource(R.drawable.ic_unstar)
        }

        // Implémenter la fonction pour like
        holder.starIcon.setOnClickListener {
        // Inverse si le bouton est déjà liké ou non
        currentRecipe.liked = !currentRecipe.liked
        // Mettre à jour l'objet recette
            repo.updateRecipe(currentRecipe)
}
        //interraction lors du clic sur une recette
        holder.itemView.setOnClickListener {
            // Afficher la popup
            RecipePopup(this,currentRecipe).show()
        }
    }
}
