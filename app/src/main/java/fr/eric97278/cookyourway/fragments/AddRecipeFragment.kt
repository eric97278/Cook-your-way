package fr.eric97278.cookyourway.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import fr.eric97278.cookyourway.MainActivity
import fr.eric97278.cookyourway.R
import fr.eric97278.cookyourway.RecipeRepository

class AddRecipeFragment(
    private val context: MainActivity
) : Fragment() {

    private var uploadedImage: ImageView? = null

    // Déclaration de l'ActivityResultLauncher
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        // récupérer uploadedImage pour lui associer son composant
        uploadedImage = view.findViewById(R.id.preview_image)

        // récupérer le bouton pour charger l'image
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

        // initialiser le launcher pour choisir l'image
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val selectedImageUri: Uri? = data?.data

                // Vérifier si l'image est bien sélectionnée
                if (selectedImageUri != null) {
                    // Afficher l'image sélectionnée dans l'ImageView
                    uploadedImage?.setImageURI(selectedImageUri)

                    // Héberger l'image sur Firebase Storage
                    val repo = RecipeRepository()
                    repo.uploadImage(selectedImageUri,
                        onSuccess = { imageUrl ->
                            Toast.makeText(context, "Image uploadée avec succès !", Toast.LENGTH_SHORT).show()
                            // Traiter l'URL de l'image pour la sauvegarder avec la recette, etc.
                        },
                        onFailure = { exception ->
                            Toast.makeText(context, "Erreur lors de l'upload : ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    Toast.makeText(context, "Aucune image sélectionnée", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Lorsque l'on clique dessus, cela ouvre la galerie pour choisir une image
        pickupImageButton.setOnClickListener { pickupImage() }

        return view
    }

    private fun pickupImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        imagePickerLauncher.launch(Intent.createChooser(intent, "Sélectionnez une image"))
    }
}
