package com.example.cookbook

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.databinding.ActivityMyRecipesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MyRecipesActivity : AppCompatActivity() {
    private var _binding: ActivityMyRecipesBinding? = null
    private val binding: ActivityMyRecipesBinding
        get() = _binding ?: throw IllegalStateException("Binding in MyRecipes Activity must not be null")
    private val listItem = ArrayList<String>()
    private val listIdItem = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener{
            finish()
        }

        binding.newRecipe.setOnClickListener{
            if (binding.newRecipe.visibility == View.VISIBLE)
                startActivity(Intent(this@MyRecipesActivity, AddNewRecipeActivity::class.java))
        }

        binding.recipes.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@MyRecipesActivity, CompleteRecipeActivity::class.java)
            intent.putExtra("id", listIdItem.get(position))
            intent.putExtra("my", true)
            startActivity(intent)
        }

        val db = Firebase.firestore
        val sp = getSharedPreferences("email and password", MODE_PRIVATE)

        db.collection("recipes")
            .whereEqualTo("who", sp.getString("id", "null"))
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    binding.recipes.visibility = View.INVISIBLE
                    binding.noRecipes.visibility = View.VISIBLE
                    binding.newRecipe.visibility = View.VISIBLE

                } else {

                    for (recipe in result) {
                        listIdItem.add(recipe.id)
                        listItem.add(recipe.get("name").toString())
                    }
                    binding.recipes.adapter = ArrayAdapter<String>(this@MyRecipesActivity,
                        R.layout.list_item, R.id.item, listItem)

                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@MyRecipesActivity,
                    "Не получилось, попробуйте позже",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }
}