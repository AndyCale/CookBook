package com.example.cookbook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.cookbook.databinding.ActivityCompleteRecipeBinding
import com.example.cookbook.databinding.ActivityMyRecipesBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CompleteRecipeActivity : AppCompatActivity() {
    private var _binding: ActivityCompleteRecipeBinding? = null
    private val binding: ActivityCompleteRecipeBinding
        get() = _binding ?: throw IllegalStateException("Binding in CompleteRecipes Activity must not be null")
    val bd = Firebase.firestore
    var countLike: Int? = null
    var flag = 0
    var favor = false
    private lateinit var idRecipe: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCompleteRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener{
            finish()
        }

        val idRecipeAsk = intent.getStringExtra("id")
        val my = intent.getBooleanExtra("my", false)

        if (my) {
            binding.dontLike.visibility = View.GONE
            binding.countLike.visibility = View.GONE
            binding.like.visibility = View.GONE
            binding.favorite.visibility = View.GONE
            binding.change.visibility = View.VISIBLE
        }

        if (idRecipeAsk != null) {
            idRecipe = idRecipeAsk

            binding.back.setOnClickListener{
                val sp = getSharedPreferences("email and password", Context.MODE_PRIVATE)

                bd.collection("recipes").document(idRecipe).update("rating", countLike)
                if (favor) {
                    bd.collection("users")
                        .document(sp.getString("id", "noname").toString())
                        .update("likes", FieldValue.arrayUnion(idRecipe))
                }
                finish()
            }

            binding.dontLike.setOnClickListener {
                if (countLike != null && flag != -1) {
                    binding.dontLike.setBackgroundResource(R.drawable.thumb_down_alt)
                    binding.like.setBackgroundResource(R.drawable.thumb_up_off_alt)
                    countLike = countLike!! - 1 - flag
                    binding.countLike.text = countLike.toString()
                    flag = -1
                }
                else if (countLike != null) {
                    binding.dontLike.setBackgroundResource(R.drawable.thumb_down_off_alt)
                    countLike = countLike!! + 1
                    binding.countLike.text = countLike.toString()
                    flag = 0
                }
            }
            binding.like.setOnClickListener {
                if (countLike != null && flag != 1) {
                    binding.dontLike.setBackgroundResource(R.drawable.thumb_down_off_alt)
                    binding.like.setBackgroundResource(R.drawable.thumb_up_alt)
                    countLike = countLike!! + 1 - flag
                    binding.countLike.text = countLike.toString()
                    flag = 1
                }
                else if (countLike != null) {
                    binding.like.setBackgroundResource(R.drawable.thumb_up_off_alt)
                    countLike = countLike!! - 1
                    binding.countLike.text = countLike.toString()
                    flag = 0
                }
            }
            binding.favorite.setOnClickListener {
                if (favor) {
                    favor = false
                    binding.favorite.setBackgroundResource(R.drawable.not_favorites)
                }
                else {
                    favor = true
                    binding.favorite.setBackgroundResource(R.drawable.favorite)
                }
            }

            binding.change.setOnClickListener {

            }

            bd.collection("recipes")
                .document(idRecipe)
                .get()
                .addOnSuccessListener { result ->
                    binding.titleText.text = result.get("name").toString()
                    binding.ingredients.text = result.get("ingredients").toString()
                    binding.description.text = result.get("description").toString()
                    binding.category.text = result.get("category").toString()
                    binding.countLike.text = result.get("rating").toString()
                    countLike = result.get("rating").toString().toInt()
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val sp = getSharedPreferences("email and password", Context.MODE_PRIVATE)

        bd.collection("recipes").document(idRecipe).update("rating", countLike)
        if (favor) {
            bd.collection("users")
                .document(sp.getString("id", "noname").toString())
                .update("likes", FieldValue.arrayUnion(idRecipe))
        }
        super.onBackPressed()
        finish()

    }
}