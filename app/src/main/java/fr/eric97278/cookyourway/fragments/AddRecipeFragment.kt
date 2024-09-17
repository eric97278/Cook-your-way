package fr.eric97278.cookyourway.fragments

import android.app.Activity
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
import java.util.UUID // Utiliser java.util.UUID

class AddRecipeFragment(
    private val context: MainActivity
) : Fragment() {
    private var file: Uri? = null
    private var uploadedImage: ImageView? = null

    // Déclaration de l'ActivityResultLauncher
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        // Récupérer l'ImageView pour prévisualiser l'image
        uploadedImage = view.findViewById(R.id.preview_image)

        // Récupérer le bouton pour charger l'image
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

        // Initialiser le launcher pour choisir l'image
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    val selectedImageUri: Uri? = data?.data

                    // Vérifier si l'image est bien sélectionnée
                    if (selectedImageUri != null) {
                        // Afficher l'image sélectionnée dans l'ImageView
                        uploadedImage?.setImageURI(selectedImageUri)
                        // Stocker l'URI dans la variable file pour un usage ultérieur
                        file = selectedImageUri
                    } else {
                        Toast.makeText(context, "Aucune image sélectionnée", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        // Lorsque l'on clique dessus, cela ouvre la galerie pour choisir une image
        pickupImageButton.setOnClickListener { pickupImage() }

        // Récupérer le bouton confirmer
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener { sendForm(view) }

        return view
    }

    private fun sendForm(view: View) {
        // Vérifier que le fichier de l'image est bien sélectionné
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

        // Héberger l'image sur Firebase Storage
        val repo = RecipeRepository()
        file?.let {
            repo.uploadImage(it,
                onSuccess = { imageUrl ->
                    // Créer un nouvel objet RecipeModel avec toutes les informations
                    val newRecipe = RecipeModel(
                        id = UUID.randomUUID().toString(), // Utiliser java.util.UUID
                        name = recipeName,
                        description = recipeDescription,
                        difficulty = difficulty,
                        time = time,
                        ingredients = ingredients,
                        steps = steps,
                        imageUrl = imageUrl.toString() // L'URL de l'image hébergée
                    )

                    // Enregistrer la recette dans Firebase Database
                    repo.addRecipe(newRecipe)  // Utiliser addRecipe

                    Toast.makeText(context, "Recette ajoutée avec succès !", Toast.LENGTH_SHORT).show()

                    // Réinitialiser le formulaire ou revenir à une autre page
                    resetForm(view)
                },
                onFailure = { exception ->
                    Toast.makeText(context, "Erreur lors de l'upload : ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    // Réinitialiser le formulaire après l'envoi
    private fun resetForm(view: View) {
        view.findViewById<EditText>(R.id.name_input).text.clear()
        view.findViewById<EditText>(R.id.description_input).text.clear()
        view.findViewById<EditText>(R.id.ingredient_input).text.clear()
        view.findViewById<EditText>(R.id.steps_input).text.clear()
        uploadedImage?.setImageResource(R.drawable.image_default)  // Réinitialiser l'image par défaut
        file = null
    }

    private fun pickupImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imagePickerLauncher.launch(Intent.createChooser(intent, "Sélectionnez une image"))
    }
}
