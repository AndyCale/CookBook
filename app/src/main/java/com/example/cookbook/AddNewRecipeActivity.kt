package com.example.cookbook

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.databinding.ActivityAddNewRecipeBinding
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

        binding.back.setOnClickListener{
            finish()
        }

        binding.create.setOnClickListener {
            if (binding.titleText.text.isEmpty())
                Toast.makeText(this@AddNewRecipeActivity,
                    "Пожалуйста, введите название рецепта", Toast.LENGTH_SHORT).show()
            else if (binding.ingredients.text.isEmpty())
                Toast.makeText(this@AddNewRecipeActivity,
                    "Пожалуйста, введите необходимые ингредиенты", Toast.LENGTH_SHORT).show()
            else if (binding.description.text.isEmpty())
                Toast.makeText(this@AddNewRecipeActivity,
                    "Пожалуйста, заполните описание приготовления", Toast.LENGTH_SHORT).show()
            else {
                binding.create.isClickable = false
                addNewRecipe()
            }
        }
    }

    fun addNewRecipe() {
        val db = Firebase.firestore
        val sp = getSharedPreferences("email and password", Context.MODE_PRIVATE)
        db.collection("recipes")

        val recipe = hashMapOf(
            "name" to binding.titleText.text.toString(),
            "ingredients" to binding.ingredients.text.toString(),
            "description" to binding.description.text.toString(),
            "who" to sp.getString("id", "Noname"),
            "category" to binding.category.selectedItem.toString(),
            "rating" to 0
        )
        Toast.makeText(
            this@AddNewRecipeActivity,
            "Новый рецепт загружается, подождите",
            Toast.LENGTH_SHORT
        ).show()

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