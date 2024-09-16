package fr.eric97278.cookyourway

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
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


    }

    private fun setupComponents() {
        //actualiser l'image de la recette
        val recipeImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentRecipe.imageUrl)).into(recipeImage)
    }

}