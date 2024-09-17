package fr.eric97278.cookyourway

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.eric97278.cookyourway.fragments.AddRecipeFragment
import fr.eric97278.cookyourway.fragments.CollectionFragment
import fr.eric97278.cookyourway.fragments.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        // Importer la BottomNavigationView
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener {it: MenuItem ->
            when (it.itemId) {
                R.id.home_page_item -> {
                    loadFragment(HomeFragment(this),R.string.home_page_title)
                    true
                }
                R.id.collection_page_item -> {
                    loadFragment(CollectionFragment(this),R.string.collection_page_title)
                    true
                }
                R.id.add_recipe_page_item -> {
                    loadFragment(AddRecipeFragment(this),R.string.add_recipe_page_title)
                    true
                }
                else -> false
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Charger le fragment par défaut si nécessaire
        if (savedInstanceState == null) {
            loadFragment(HomeFragment(this),R.string.home_page_title)
        }
    }

    private fun loadFragment(fragment: Fragment,string :Int) {
        // Charger le repository
        val repo = RecipeRepository()

        // Mettre à jour le titre de la page
        findViewById<TextView>(R.id.page_title).text = resources.getString(string)

        // Mettre à jour la liste de recettes
        repo.updateData {
            // Injecter le fragment dans une boite (fragment_container)
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
