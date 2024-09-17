package fr.eric97278.cookyourway

import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class RecipeRepository {

    object Singleton {
        private const val BUCKET_URL: String = "gs://cook-your-way-7ed5e.appspot.com"

        var storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        var databaseRef = FirebaseDatabase.getInstance().getReference("recipes")
        var recipeList = arrayListOf<RecipeModel>()
    }

    // Ajouter une recette à la base de données avec une clé séquentielle
    fun addRecipe(recipe: RecipeModel) {
        // Obtenir la clé suivante
        getNextRecipeId { nextId ->
            if (nextId != null) {
                recipe.id = nextId
                Singleton.databaseRef.child(nextId).setValue(recipe)
                    .addOnSuccessListener {
                        Log.d("RecipeRepository", "Recette ajoutée avec succès")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("RecipeRepository", "Erreur lors de l'ajout de la recette : ${exception.message}")
                    }
            }
        }
    }

    // Méthode pour obtenir la clé suivante dans la séquence
    private fun getNextRecipeId(callback: (String?) -> Unit) {
        Singleton.databaseRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lastKey = snapshot.children.firstOrNull()?.key
                val nextId = if (lastKey != null && lastKey.startsWith("recipe")) {
                    val number = lastKey.removePrefix("recipe").toIntOrNull()
                    if (number != null) {
                        "recipe${number + 1}"
                    } else {
                        "recipe1"
                    }
                } else {
                    "recipe1"
                }
                callback(nextId)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RecipeRepository", "Erreur lors de l'obtention de la clé suivante : ${error.message}")
                callback(null)
            }
        })
    }

    fun updateData(callback: () -> Unit) {
        Singleton.databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Singleton.recipeList.clear()
                for (ds in snapshot.children) {
                    val recipeData = ds.value
                    Log.d("RecipeData", recipeData.toString())

                    try {
                        val recipe = ds.getValue(RecipeModel::class.java)
                        if (recipe != null) {
                            Singleton.recipeList.add(recipe)
                        }
                    } catch (e: Exception) {
                        Log.e("RecipeData", "Erreur de conversion : ${e.message}")
                    }
                }
                callback()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("RecipeRepository", "Erreur de lecture : ${error.message}")
            }
        })
    }

    fun uploadImage(file: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = Singleton.storageReference.child(fileName)

        ref.putFile(file)
            .addOnSuccessListener { taskSnapshot ->
                ref.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }.addOnFailureListener { exception ->
                    onFailure(exception)
                }
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun updateRecipe(recipe: RecipeModel) {
        Singleton.databaseRef.child(recipe.id).setValue(recipe)
            .addOnSuccessListener {
                Log.d("RecipeRepository", "Recette mise à jour avec succès")
            }
            .addOnFailureListener { exception ->
                Log.e("RecipeRepository", "Erreur lors de la mise à jour de la recette : ${exception.message}")
            }
    }

    fun deleteRecipe(recipe: RecipeModel) {
        Singleton.databaseRef.child(recipe.id).removeValue()
            .addOnSuccessListener {
                Log.d("RecipeRepository", "Recette supprimée avec succès")
            }
            .addOnFailureListener { exception ->
                Log.e("RecipeRepository", "Erreur lors de la suppression de la recette : ${exception.message}")
            }
    }
}
