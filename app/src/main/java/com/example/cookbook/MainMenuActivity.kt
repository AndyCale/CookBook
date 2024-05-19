package com.example.cookbook

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.cookbook.databinding.ActivityMainMenuBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainMenuActivity : AppCompatActivity() {
    private var _binding: ActivityMainMenuBinding? = null
    private val binding: ActivityMainMenuBinding
        get() = _binding ?: throw IllegalStateException("Binding in MainMenu Activity must not be null")

    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var sp = getSharedPreferences("email and password", Context.MODE_PRIVATE)
        sp.edit().putString("TY", "9").commit()

        if (sp.getString("id", "Noname") == "Noname") {
            findIdUser()
        }

        binding.fullName.text = sp.getString("fullName", "Произошла непредвиденная ошибка")

        binding.logOut.setOnClickListener {
            sp.edit().putString("TY", "null").commit()
            val intent = Intent(this@MainMenuActivity,
                MainActivity::class.java)
            startActivity(intent)
        }

        binding.myRecipes.setOnClickListener {
            Toast.makeText(this, "Рецепты загружаются...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MainMenuActivity, MyRecipesActivity::class.java))
        }

        binding.addRecipe.setOnClickListener {
            startActivity(Intent(this@MainMenuActivity, AddNewRecipeActivity::class.java))
        }

        binding.moreRecipes.setOnClickListener {
            Toast.makeText(this, "Рецепты загружаются...", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MainMenuActivity, MoreRecipesActivity::class.java))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        }
        else {
            backPressedTime = System.currentTimeMillis()
            Toast.makeText(this, "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show()
        }
    }

    fun findIdUser() {
        val db = Firebase.firestore
        var sp = getSharedPreferences("email and password", Context.MODE_PRIVATE)

        db.collection("users")
            .whereEqualTo("fullName", sp.getString("fullName", "Noname"))
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    Toast.makeText(
                        this@MainMenuActivity,
                        "Данный пользователь не найден, попробуйте перезайти", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    for (product in result) {
                        sp.edit().putString("id", product.id).commit()
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@MainMenuActivity,
                    "Не получилось, попробуйте позже",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
