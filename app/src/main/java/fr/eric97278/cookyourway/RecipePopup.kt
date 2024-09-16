package fr.eric97278.cookyourway

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import fr.eric97278.cookyourway.adapter.RecipeAdapter

class RecipePopup(private val adapter: RecipeAdapter
) : Dialog(adapter.context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ne pas afficher le titre puisqu'il est dans le layout
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //afficher la popup
        setContentView(R.layout.popup_recipe_details)
    }

}