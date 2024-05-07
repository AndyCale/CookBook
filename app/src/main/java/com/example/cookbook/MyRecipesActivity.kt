package com.example.cookbook

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
/*
    private fun createNewTextView(text: String): TextView? {
        val lparams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        val textView = TextView(this)
        textView.layoutParams = lparams
        textView.text = "New text: $text"
        return textView
    }

 */
}