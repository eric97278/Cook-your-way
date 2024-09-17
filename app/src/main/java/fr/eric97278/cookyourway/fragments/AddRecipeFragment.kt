package fr.eric97278.cookyourway.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import fr.eric97278.cookyourway.MainActivity
import fr.eric97278.cookyourway.R
import fr.eric97278.cookyourway.RecipeModel
import fr.eric97278.cookyourway.RecipeRepository

class AddRecipeFragment(
    private val context: MainActivity
) : Fragment() {
    private var file: Uri? = null
    private var uploadedImage: ImageView? = null

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        uploadedImage = view.findViewById(R.id.preview_image)
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

        // Initialiser le launcher pour récupérer l'image
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        uploadedImage?.setImageURI(selectedImageUri)
                        file = selectedImageUri
                    } else {
                        Toast.makeText(context, "Aucune image sélectionnée", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        // Lancement de la sélection d'image
        pickupImageButton.setOnClickListener { pickupImage() }

        // Bouton de confirmation de la recette
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener { sendForm(view) }

        return view
    }

    // Envoi du formulaire pour ajouter une recette
    private fun sendForm(view: View) {
        if (file == null) {
            Toast.makeText(context, "Veuillez sélectionner une image", Toast.LENGTH_SHORT).show()
            return
        }

        // Récupérer les informations du formulaire
        val recipeName = view.findViewById<EditText>(R.id.name_input).text.toString()
        val recipeDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
        val difficulty = view.findViewById<Spinner>(R.id.difficulty_spinner).selectedItem.toString()
        val time = view.findViewById<Spinner>(R.id.duration_spinner).selectedItem.toString()
        val ingredients = view.findViewById<EditText>(R.id.ingredient_input).text.toString()
        val steps = view.findViewById<EditText>(R.id.steps_input).text.toString()

        // Vérifier que tous les champs sont remplis
        if (recipeName.isEmpty() || recipeDescription.isEmpty() || ingredients.isEmpty() || steps.isEmpty()) {
            Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        // Téléchargement de l'image et création de la recette
        val repo = RecipeRepository()
        file?.let {
            repo.uploadImage(it,
                onSuccess = { imageUrl ->
                    // Générer un nouvel ID de recette dynamique
                    val newRecipe = RecipeModel(
                        id = getNextRecipeId(context), // Générer un ID unique
                        name = recipeName,
                        description = recipeDescription,
                        difficulty = difficulty,
                        time = time,
                        ingredients = ingredients,
                        steps = steps,
                        imageUrl = imageUrl
                    )

                    // Ajouter la recette dans Firebase
                    repo.addRecipe(newRecipe)

                    Toast.makeText(context, "Recette ajoutée avec succès !", Toast.LENGTH_SHORT).show()
                    resetForm(view) // Réinitialiser le formulaire après l'envoi
                },
                onFailure = { exception ->
                    Toast.makeText(context, "Erreur lors de l'upload : ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    // Réinitialisation du formulaire
    private fun resetForm(view: View) {
        view.findViewById<EditText>(R.id.name_input).text.clear()
        view.findViewById<EditText>(R.id.description_input).text.clear()
        view.findViewById<EditText>(R.id.ingredient_input).text.clear()
        view.findViewById<EditText>(R.id.steps_input).text.clear()
        uploadedImage?.setImageResource(R.drawable.image_default)
        file = null
    }

    // Lancer la sélection d'image
    private fun pickupImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imagePickerLauncher.launch(Intent.createChooser(intent, "Sélectionnez une image"))
    }

    // Méthode pour générer un ID unique pour chaque nouvelle recette
    private fun getNextRecipeId(context: Context): String {
        // Utilisation de SharedPreferences pour stocker l'ID courant
        val sharedPref = context.getSharedPreferences("recipe_prefs", Context.MODE_PRIVATE)
        val currentId = sharedPref.getInt("recipe_counter", 0) // Récupérer l'ID actuel
        val newId = currentId + 1 // Incrémenter l'ID

        // Sauvegarder le nouvel ID dans SharedPreferences
        with(sharedPref.edit()) {
            putInt("recipe_counter", newId)
            apply()
        }

        // Retourner le nouvel ID sous forme de "recipe1", "recipe2", ...
        return "recipe$newId"
    }
}
