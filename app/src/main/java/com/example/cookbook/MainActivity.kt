package com.example.cookbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import com.example.cookbook.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.File


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding ?: throw IllegalStateException("Binding in Main Activity must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val dexOutputDir: File = codeCacheDir
        dexOutputDir.setReadOnly()

        /*
        binding.showPassword.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> binding.password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    MotionEvent.ACTION_UP -> binding.password.inputType = 129
                }
                return v?.onTouchEvent(event) ?: true
            }
        })

        */

        binding.showPassword.setOnClickListener {
            if (binding.password.inputType != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                binding.password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else binding.password.inputType = 129
        }


        val sp = getSharedPreferences("email and password", MODE_PRIVATE)
        if (sp.getString("TY", "null") != "null") {
            val intent = Intent(this@MainActivity, MainMenuActivity::class.java)
            startActivity(intent)
        }
        else {
            with (binding) {
                signUp.setOnClickListener {
                    val intent = Intent(this@MainActivity, SignUpActivity::class.java)
                    startActivity(intent)
                }

                val db = Firebase.firestore

                signIn.setOnClickListener {
                    db.collection("users")
                        .get()
                        .addOnSuccessListener { result ->
                            if (result.isEmpty)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Данный пользователь не найден", Toast.LENGTH_SHORT).show()
                            else {
                                for (document in result) {
                                    if (document.getString("email") == email.text.toString()) {
                                        if (document.getString("password") == password.text.toString()) {
                                            sp.edit().putString(
                                                "fullName",
                                                document.getString("fullName")
                                            ).commit()
                                            sp.edit().putString(
                                                "id",
                                                document.id
                                            ).commit()
                                            val intent = Intent(
                                                this@MainActivity,
                                                MainMenuActivity::class.java
                                            )
                                            startActivity(intent)
                                        } else {
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Неправильный пароль!", Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this@MainActivity,
                                "Произошла ошибка, попробуйте позже", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }
}


