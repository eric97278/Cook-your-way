package fr.eric97278.cookyourway.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.eric97278.cookyourway.MainActivity
import fr.eric97278.cookyourway.R
import fr.eric97278.cookyourway.RecipeRepository.Singleton.recipeList
import fr.eric97278.cookyourway.adapter.RecipeAdapter
import fr.eric97278.cookyourway.adapter.RecipeItemDecoration

class CollectionFragment (
    private val context: MainActivity
) : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_collection, container, false)

        //recuperer recyclerview
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.collection_recycler_list)
        collectionRecyclerView.adapter = RecipeAdapter(context, recipeList.filter { it.liked }, R.layout.item_vertical_recipe)
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        collectionRecyclerView.addItemDecoration(RecipeItemDecoration())

        return view
    }

}