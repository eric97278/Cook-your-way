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
        //donner le lien pour acceder au bucket
        private val BUCKET_URL: String = "gs://cook-your-way-7ed5e.appspot.com"

        //se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

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
    //créer une fonction pour envoyer des chichier sur le storage
    fun uploadImage(file: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        // Générer un nom de fichier unique
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val ref = storageReference.child(fileName)

        // Démarrer la tâche d'envoi
        val uploadTask = ref.putFile(file)

        // Gérer le succès ou l'échec de l'envoi
        uploadTask.addOnSuccessListener { taskSnapshot ->
            // Récupérer l'URL de téléchargement
            ref.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())  // Renvoie l'URL de téléchargement via le callback
            }.addOnFailureListener { exception ->
                onFailure(exception)  // Gérer une éventuelle erreur dans la récupération de l'URL
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)  // Gérer l'erreur lors de l'upload
        }
    }

    //mettre a jour un objet recette en base de données
    fun updateRecipe(recipe: RecipeModel) {
        databaseRef.child(recipe.id).setValue(recipe)
    }

    //supprimer un objet recette de la base de données
    fun deleteRecipe(recipe: RecipeModel)= databaseRef.child(recipe.id).removeValue()


}