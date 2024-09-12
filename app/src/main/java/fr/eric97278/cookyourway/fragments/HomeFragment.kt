package fr.eric97278.cookyourway.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.eric97278.cookyourway.R
import fr.eric97278.cookyourway.adapter.RecipeAdapter
import fr.eric97278.cookyourway.adapter.RecipeItemDecoration

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //recuperer le recyclerView
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = RecipeAdapter(R.layout.item_horizontal_recipe)

        //recuperer le second recyclerview
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = RecipeAdapter(R.layout.item_vertical_recipe)
        verticalRecyclerView.addItemDecoration(RecipeItemDecoration())



        return view


    }
}