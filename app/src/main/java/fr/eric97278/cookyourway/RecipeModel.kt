package fr.eric97278.cookyourway

class RecipeModel (
    var id: String ="",
    val name: String="",
    val description: String="",
    val ingredients: String="",
    val imageUrl: String="",
    val difficulty: String="",
    val time: String="",
    val steps: String="",
    var liked: Boolean = false
)