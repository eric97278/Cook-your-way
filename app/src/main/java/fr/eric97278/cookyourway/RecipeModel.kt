package fr.eric97278.cookyourway

class RecipeModel (
    val id: String,
    val name: String,
    val description: String,
    val ingredients: ArrayList<String>,
    val imageURL: String,
    val difficulty: String,
    val time: String,
    val steps: String,
    val liked: Boolean
)