package com.example.cookbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cookbook.databinding.ActivityAddNewRecipeBinding
import com.example.cookbook.databinding.ActivityMyRecipesBinding

class MyRecipesActivity : AppCompatActivity() {
    private var _binding: ActivityMyRecipesBinding? = null
    private val binding: ActivityMyRecipesBinding
        get() = _binding ?: throw IllegalStateException("Binding in MyRecipes Activity must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}