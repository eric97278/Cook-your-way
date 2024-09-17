package fr.eric97278.cookyourway

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class RecipePopup(
    private val context: MainActivity,
    private val currentRecipe: RecipeModel
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_recipe_details)
        setupComponents()
        setupCloseButton()
        setupDeleteButton()
        setupStarButton()
    }

    private fun updateStar(button: ImageView) {
        if (currentRecipe.liked) {
            button.setImageResource(R.drawable.ic_star)
        } else {
            button.setImageResource(R.drawable.ic_unstar)
        }
    }

    private fun setupStarButton() {
        val starButton = findViewById<ImageView>(R.id.star_button)
        updateStar(starButton)

        starButton.setOnClickListener {
            currentRecipe.liked = !currentRecipe.liked
            val repo = RecipeRepository()
            repo.updateRecipe(currentRecipe)
            updateStar(starButton)
        }
    }

    private fun setupDeleteButton() {
        findViewById<ImageView>(R.id.delete_button).setOnClickListener {
            val repo = RecipeRepository()
            repo.deleteRecipe(currentRecipe)
            dismiss()
        }
    }

    private fun setupCloseButton() {
        findViewById<ImageView>(R.id.close_button).setOnClickListener {
            dismiss()
        }
    }

    private fun setupComponents() {
        val recipeImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(context).load(Uri.parse(currentRecipe.imageUrl)).into(recipeImage)

        findViewById<TextView>(R.id.popup_recipe_name).text = currentRecipe.name
        findViewById<TextView>(R.id.popup_recipe_description_subtitle).text = currentRecipe.description
        findViewById<TextView>(R.id.popup_recipe_difficulty_subtitle).text = currentRecipe.difficulty
        findViewById<TextView>(R.id.popup_recipe_duration_subtitle).text = currentRecipe.time
        findViewById<TextView>(R.id.popup_recipe_ingredient_details).text = currentRecipe.ingredients
        findViewById<TextView>(R.id.popup_recipe_steps_details).text = currentRecipe.steps
    }
}
