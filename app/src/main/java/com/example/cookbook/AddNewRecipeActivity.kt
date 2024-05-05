package com.example.cookbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cookbook.databinding.ActivityAddNewRecipeBinding
import com.example.cookbook.databinding.ActivityMainMenuBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddNewRecipeActivity : AppCompatActivity() {
    private var _binding: ActivityAddNewRecipeBinding? = null
    private val binding: ActivityAddNewRecipeBinding
        get() = _binding ?: throw IllegalStateException("Binding in AddNewRecipe Activity must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAddNewRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.create.setOnClickListener {
            if (binding.titleText.text.isEmpty() || binding.ingredients.text.isEmpty() ||
                binding.description.text.isEmpty()) {
                Toast.makeText(this@AddNewRecipeActivity,
                    "Пожалуйста, заполните все поля рецепта", Toast.LENGTH_SHORT).show()
            }
            else {
                addNewRecipe()
            }
        }
    }

    fun addNewRecipe() {
        val db = Firebase.firestore

        val recipe = hashMapOf(
            "name" to binding.titleText.text.toString(),
            "ingredients" to binding.ingredients.text.toString(),
            "description" to binding.description.text.toString()
        )

        db.collection("recipes")
            .add(recipe)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(
                    this@AddNewRecipeActivity,
                    "Рецепт добавлен",
                    Toast.LENGTH_SHORT
                ).show()
                val intent =
                    Intent(this@AddNewRecipeActivity, MainMenuActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@AddNewRecipeActivity,
                    "Не получилось, попробуйте позже",
                    Toast.LENGTH_SHORT
                ).show()
                val intent =
                    Intent(this@AddNewRecipeActivity, MainActivity::class.java)
                startActivity(intent)
            }
    }
}