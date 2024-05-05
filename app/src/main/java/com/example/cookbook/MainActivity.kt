package com.example.cookbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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

        var sp = getSharedPreferences("email and password", MODE_PRIVATE)
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

                var db = Firebase.firestore
                var flag = false

                signIn.setOnClickListener {
                    db.collection("users")
                        .get()
                        .addOnSuccessListener { result ->
                            for (document in result) {
                                if (document.getString("email") == email.text.toString()) {
                                    if (document.getString("password") == password.text.toString()) {
                                        flag = true
                                        sp.edit().putString("fullName",
                                            document.getString("fullName")).commit()
                                        val intent = Intent(this@MainActivity,
                                            MainMenuActivity::class.java)
                                        startActivity(intent)
                                    }
                                    else {
                                        flag = true
                                        Toast.makeText(this@MainActivity,
                                            "Неправильный пароль!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this@MainActivity,
                                "Произошла ошибка, попробуйте позже", Toast.LENGTH_SHORT).show()
                        }

                    Handler().postDelayed({
                        if (!flag) {
                            Toast.makeText(
                                this@MainActivity,
                                "Данный пользователь не найден", Toast.LENGTH_SHORT).show()
                        }
                    }, 1000)
                }
            }
        }
    }
}

