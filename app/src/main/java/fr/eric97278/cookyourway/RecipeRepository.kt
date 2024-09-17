package fr.eric97278.cookyourway

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import fr.eric97278.cookyourway.RecipeRepository.Singleton.databaseRef
import fr.eric97278.cookyourway.RecipeRepository.Singleton.recipeList
import fr.eric97278.cookyourway.RecipeRepository.Singleton.storageReference
import java.util.UUID
import kotlin.coroutines.Continuation

class RecipeRepository {
    object Singleton {
        // Se connecter au bucket Firebase Storage
        private val BUCKET_URL: String = "gs://cook-your-way-7ed5e.appspot.com"
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // Se connecter à la référence "recipes" dans Firebase Realtime Database
        val databaseRef = FirebaseDatabase.getInstance().getReference("recipes")

        // Liste des recettes
        val recipeList = arrayListOf<RecipeModel>()
    }

    // Ajouter une recette à la base de données avec `push()`
    fun addRecipe(recipe: RecipeModel) {
        databaseRef.push().setValue(recipe)
            .addOnSuccessListener {
                Log.d("RecipeRepository", "Recette ajoutée avec succès")
            }
            .addOnFailureListener { exception ->
                Log.e("RecipeRepository", "Erreur lors de l'ajout de la recette : ${exception.message}")
            }
    }

    // Mettre à jour les données
    fun updateData(callback: () -> Unit) {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recipeList.clear()
                for (ds in snapshot.children) {
                    val recipe = ds.getValue(RecipeModel::class.java)
                    if (recipe != null) {
                        recipeList.add(recipe)
                    }
                }
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    // Fonction pour télécharger une image dans Firebase Storage
    fun uploadImage(file: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = storageReference.child(fileName)
        ref.putFile(file)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }.addOnFailureListener { onFailure(it) }
            }
            .addOnFailureListener { onFailure(it) }
    }

    // Mettre à jour une recette
    fun updateRecipe(recipe: RecipeModel) {
        databaseRef.child(recipe.id).setValue(recipe)
    }

    // Supprimer une recette
    fun deleteRecipe(recipe: RecipeModel) = databaseRef.child(recipe.id).removeValue()
}
