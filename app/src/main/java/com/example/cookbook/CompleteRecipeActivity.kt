package com.example.cookbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cookbook.databinding.ActivityCompleteRecipeBinding
import com.example.cookbook.databinding.ActivityMyRecipesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CompleteRecipeActivity : AppCompatActivity() {
    private var _binding: ActivityCompleteRecipeBinding? = null
    private val binding: ActivityCompleteRecipeBinding
        get() = _binding ?: throw IllegalStateException("Binding in CompleteRecipes Activity must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCompleteRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener{
            finish()
        }

        val idRecipe = intent.getStringExtra("id")
        if (idRecipe != null) {
            val bd = Firebase.firestore

            bd.collection("recipes")
                .document(idRecipe)
                .get()
                .addOnSuccessListener { result ->
                    binding.titleText.text = result.get("name").toString()
                    binding.ingredients.text = result.get("ingredients").toString()
                    binding.description.text = result.get("description").toString()
                    binding.category.text = result.get("category").toString()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this@CompleteRecipeActivity,
                        "Не получилось, попробуйте позже",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent =
                        Intent(this@CompleteRecipeActivity, MainActivity::class.java)
                    startActivity(intent)
                }
        }
        else {
            Toast.makeText(this@CompleteRecipeActivity, "Что-то пошло не так...", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}