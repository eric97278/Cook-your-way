package fr.eric97278.cookyourway

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import fr.eric97278.cookyourway.adapter.RecipeAdapter

class RecipePopup(
    private val adapter: RecipeAdapter,
    private val currentRecipe: RecipeModel
) : Dialog(adapter.context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ne pas afficher le titre puisqu'il est dans le layout
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //afficher la popup
        setContentView(R.layout.popup_recipe_details)
        setupComponents()
        setupCloseButton()
        setupDeleteButton()
        setupStarButton()

    }

    private fun updateStar(button: ImageView) {
        if(currentRecipe.liked){
            button.setImageResource(R.drawable.ic_star)
        }
        else{
            button.setImageResource(R.drawable.ic_unstar)
        }
    }

    private fun setupStarButton() {
        //récupérer
        val starButton = findViewById<ImageView>(R.id.star_button)
        updateStar(starButton)


        //interaction
        starButton.setOnClickListener {
            currentRecipe.liked = !currentRecipe.liked
            val repo = RecipeRepository()
            repo.updateRecipe(currentRecipe)
            updateStar(starButton)
        }
    }

    private fun setupDeleteButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener {
            //supprimer la recette de la base de données
            val repo = RecipeRepository()
            repo.deleteRecipe(currentRecipe)
            dismiss()
        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener {
            //fermer la popup
            dismiss()
        }
    }

    private fun setupComponents() {
        //actualiser l'image de la recette
        val recipeImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentRecipe.imageUrl)).into(recipeImage)

        //actualiser le nom de la recette
        findViewById<TextView>(R.id.popup_recipe_name).text = currentRecipe.name

        //actualiser la description de la recette
        findViewById<TextView>(R.id.popup_recipe_description_subtitle).text = currentRecipe.description

        //actualiser la difficulté de la recette
        findViewById<TextView>(R.id.popup_recipe_difficulty_subtitle).text = currentRecipe.difficulty.toString()

        //actualiser le temps de la recette
        findViewById<TextView>(R.id.popup_recipe_duration_subtitle).text = currentRecipe.time.toString()

        //actualiser les ingrédients de la recette
        findViewById<TextView>(R.id.popup_recipe_ingredient_details).text = currentRecipe.ingredients

        //actualiser les étapes de la recette
        findViewById<TextView>(R.id.popup_recipe_steps_details).text = currentRecipe.steps

    }

}