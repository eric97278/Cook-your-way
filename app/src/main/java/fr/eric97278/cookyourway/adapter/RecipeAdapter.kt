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
    private val context: MainActivity,
    private val recipeList: List<RecipeModel>,
    private val layoutId: Int
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeImage = view.findViewById<ImageView>(R.id.image_item)
        val recipeName: TextView? = view.findViewById(R.id.name_item)
        val recipeDescription: TextView? = view.findViewById(R.id.description_item)
        val recipeDifficultySubtitle: TextView? = view.findViewById(R.id.difficulty_subtitle_item)
        val recipeTimeSubtitle: TextView? = view.findViewById(R.id.time_subtitle_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = recipeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRecipe = recipeList[position]
        val repo = RecipeRepository()

        holder.recipeName?.text = currentRecipe.name
        holder.recipeDescription?.text = currentRecipe.description
        holder.recipeDifficultySubtitle?.text = currentRecipe.difficulty
        holder.recipeTimeSubtitle?.text = currentRecipe.time

        Log.d("ImageURL", "URL de l'image: ${currentRecipe.imageUrl}")

        Glide.with(context)
            .load(currentRecipe.imageUrl)
            .placeholder(R.drawable.ic_image_upload)
            .error(R.drawable.ic_image_broken)
            .into(holder.recipeImage)

        if (currentRecipe.liked) {
            holder.starIcon.setImageResource(R.drawable.ic_star)
        } else {
            holder.starIcon.setImageResource(R.drawable.ic_unstar)
        }

        holder.starIcon.setOnClickListener {
            currentRecipe.liked = !currentRecipe.liked
            repo.updateRecipe(currentRecipe)
        }

        holder.itemView.setOnClickListener {
            RecipePopup(context, currentRecipe).show()
        }
    }
}
