package com.example.cookbook

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.split
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.databinding.ActivityMoreRecipesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MoreRecipesActivity : AppCompatActivity() {
    private var _binding: ActivityMoreRecipesBinding? = null
    private val binding: ActivityMoreRecipesBinding
        get() = _binding ?: throw IllegalStateException("Binding in MyRecipes Activity must not be null")
    private val listItem = ArrayList<String>()
    private val listIdItem = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMoreRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener{
            finish()
        }

        if (binding.choice.selectedItem.toString() == "Выбранная категория") {
            binding.category.visibility = View.VISIBLE
        }
        else
            binding.category.visibility = View.GONE

        binding.recipes.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@MoreRecipesActivity, CompleteRecipeActivity::class.java)
            intent.putExtra("id", listIdItem.get(position))
            intent.putExtra("my", false)
            startActivity(intent)
        }

        val db = Firebase.firestore
        val sp = getSharedPreferences("email and password", MODE_PRIVATE)

        db.collection("recipes")
            .orderBy("rating")
            .limit(60)
            .get()
            .addOnSuccessListener { result ->
                for (recipe in result) {
                    listIdItem.add(recipe.id)
                    listItem.add(recipe.get("name").toString())
                }
                binding.recipes.adapter = ArrayAdapter<String>(
                    this@MoreRecipesActivity,
                    R.layout.list_item, R.id.item, listItem
                )
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@MoreRecipesActivity,
                    "Не получилось, попробуйте позже",
                    Toast.LENGTH_SHORT
                ).show()
            }

        binding.search.setOnClickListener {
            listItem.clear()
            listIdItem.clear()
            Toast.makeText(this@MoreRecipesActivity, "Загрузка рецептов!", Toast.LENGTH_SHORT).show()

            if (binding.choice.selectedItem.toString() == "Выбранная категория") {
                db.collection("recipes")
                    .whereEqualTo("category", binding.category.selectedItem.toString())
                    .orderBy("rating")
                    .limit(60)
                    .get()
                    .addOnSuccessListener { result ->
                        for (recipe in result) {
                            listIdItem.add(recipe.id)
                            listItem.add(recipe.get("name").toString())
                        }
                        binding.recipes.adapter = ArrayAdapter<String>(
                            this@MoreRecipesActivity,
                            R.layout.list_item, R.id.item, listItem
                        )


                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this@MoreRecipesActivity,
                            "Не получилось, попробуйте позже",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            if (binding.choice.selectedItem.toString() == "Только положительный рейтинг") {
                db.collection("recipes")
                    .orderBy("rating")
                    .limit(60)
                    .get()
                    .addOnSuccessListener { result ->
                        for (recipe in result) {
                            if (recipe.get("rating").toString().toInt() > 0) {
                                listIdItem.add(recipe.id)
                                listItem.add(recipe.get("name").toString())
                            }
                        }
                        binding.recipes.adapter = ArrayAdapter<String>(
                            this@MoreRecipesActivity,
                            R.layout.list_item, R.id.item, listItem
                        )


                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this@MoreRecipesActivity,
                            "Не получилось, попробуйте позже",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            if (binding.choice.selectedItem.toString() == "Избранное") {
                db.collection("users")
                    .document(sp.getString("id", "none").toString())
                    .get()
                    .addOnSuccessListener { result ->

                        //for (recipe in result)

                        for (recipe in result.get("likes").toString().replace("[", "")
                            .replace("]", "").split(", ")) {
                            listIdItem.add(recipe.toString())
                            listItem.add(recipe.toString())

                        }
                        binding.recipes.adapter = ArrayAdapter<String>(
                            this@MoreRecipesActivity,
                            R.layout.list_item, R.id.item, listItem
                        )


                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this@MoreRecipesActivity,
                            "Не получилось, попробуйте позже",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            if (binding.choice.selectedItem.toString() == "Все") {
                db.collection("recipes")
                    .orderBy("rating")
                    .limit(60)
                    .get()
                    .addOnSuccessListener { result ->
                        for (recipe in result) {
                            listIdItem.add(recipe.id)
                            listItem.add(recipe.get("name").toString())
                        }
                        binding.recipes.adapter = ArrayAdapter<String>(
                            this@MoreRecipesActivity,
                            R.layout.list_item, R.id.item, listItem
                        )


                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this@MoreRecipesActivity,
                            "Не получилось, попробуйте позже",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }


    }
}