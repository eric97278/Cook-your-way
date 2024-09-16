package fr.eric97278.cookyourway

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import fr.eric97278.cookyourway.RecipeRepository.Singleton.databaseRef
import fr.eric97278.cookyourway.RecipeRepository.Singleton.recipeList

class RecipeRepository {
    object Singleton {
        // se connecter a la réference "recipes"
        val databaseRef = FirebaseDatabase.getInstance().getReference("recipes")

        // se créer une liste qui va contenir nos recettes
        val recipeList = arrayListOf<RecipeModel>()


    }

    fun updateData(callback: () -> Unit) {
        // absorber les données depuis la databaseRef -> liste de recettes
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer les anciennes recettes
                recipeList.clear()
                //recolter la liste
                for (ds in snapshot.children) {
                    val recipeData = ds.value
                    Log.d("RecipeData", recipeData.toString())  // Afficher les données brutes avant conversion


                    //verifier que la recette n'est pas null
                    try {
                        val recipe = ds.getValue(RecipeModel::class.java)
                        if (recipe != null) {
                            recipeList.add(recipe)
                        }
                    } catch (e: Exception) {
                        Log.e("RecipeData", "Erreur de conversion : ${e.message}")
                    }
                }
                //actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    //mettre a jour un objet recette en base de données
    fun updateRecipe(recipe: RecipeModel) {
        databaseRef.child(recipe.id).setValue(recipe)
    }

    //supprimer un objet recette de la base de données
    fun deleteRecipe(recipe: RecipeModel)= databaseRef.child(recipe.id).removeValue()


}