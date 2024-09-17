package fr.eric97278.cookyourway.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.eric97278.cookyourway.MainActivity
import fr.eric97278.cookyourway.R
import fr.eric97278.cookyourway.RecipeModel
import fr.eric97278.cookyourway.RecipeRepository.Singleton.recipeList
import fr.eric97278.cookyourway.adapter.RecipeAdapter
import fr.eric97278.cookyourway.adapter.RecipeItemDecoration

class HomeFragment (
    private val context: MainActivity
): Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)



//        //enregistrer une première recette dans la liste
//        recipeList.add(RecipeModel(
//            "Recette1",
//            "salade de fruits",
//            "Frait et bon pour la santé",
//            ingredients = arrayListOf("pommes", "bananes", "poires"),
//            imageURL = "https://media.istockphoto.com/id/1225981808/fr/photo/un-bol-de-salade-de-fruits-maison-sain-tir%C3%A9-den-haut.jpg?s=2048x2048&w=is&k=20&c=TSV3PMZzimEFqTlbii3uq5SdYuQ9Adi7apdCcu0ur84=",
//            difficulty = " Facile",
//            steps = "couper les fruits",
//            time = "10 min",
//            liked = true
//
//
//        ))
//        recipeList.add(RecipeModel(
//            "Recette1",
//            "salade de fruits",
//            "Frait et bon pour la santé",
//            ingredients = arrayListOf("pommes", "bananes", "poires"),
//            imageURL = "https://media.istockphoto.com/id/1225981808/fr/photo/un-bol-de-salade-de-fruits-maison-sain-tir%C3%A9-den-haut.jpg?s=2048x2048&w=is&k=20&c=TSV3PMZzimEFqTlbii3uq5SdYuQ9Adi7apdCcu0ur84=",
//            difficulty = " Facile",
//            steps = "couper les fruits",
//            time = "10 min",
//            liked = true
//
//
//        ))
//        recipeList.add(RecipeModel(
//            "Recette1",
//            "salade de fruits",
//            "Frait et bon pour la santé",
//            ingredients = arrayListOf("pommes", "bananes", "poires"),
//            imageURL = "https://media.istockphoto.com/id/1225981808/fr/photo/un-bol-de-salade-de-fruits-maison-sain-tir%C3%A9-den-haut.jpg?s=2048x2048&w=is&k=20&c=TSV3PMZzimEFqTlbii3uq5SdYuQ9Adi7apdCcu0ur84=",
//            difficulty = " Facile",
//            steps = "couper les fruits",
//            time = "10 min",
//            liked = true
//
//
//        ))
//        recipeList.add(RecipeModel(
//            "Recette1",
//            "salade de fruits",
//            "Frait et bon pour la santé",
//            ingredients = arrayListOf("pommes", "bananes", "poires"),
//            imageURL = "https://media.istockphoto.com/id/1225981808/fr/photo/un-bol-de-salade-de-fruits-maison-sain-tir%C3%A9-den-haut.jpg?s=2048x2048&w=is&k=20&c=TSV3PMZzimEFqTlbii3uq5SdYuQ9Adi7apdCcu0ur84=",
//            difficulty = " Facile",
//            steps = "couper les fruits",
//            time = "10 min",
//            liked = true
//
//
//        ))

        //recuperer le recyclerView
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = RecipeAdapter(context ,recipeList.filter { it.liked }, R.layout.item_horizontal_recipe)

        //recuperer le second recyclerview
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = RecipeAdapter(context,recipeList, R.layout.item_vertical_recipe)
        verticalRecyclerView.addItemDecoration(RecipeItemDecoration())



        return view


    }
}