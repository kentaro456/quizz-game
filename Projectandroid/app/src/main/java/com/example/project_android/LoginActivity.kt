package com.example.project_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private val validUsername = "user"  // Nom d'utilisateur en dur
    private val validPassword = "123"  // Mot de passe en dur

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText = findViewById<EditText>(R.id.editTextUsername)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val errorMessageTextView = findViewById<TextView>(R.id.textViewErrorMessage)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username == validUsername && password == validPassword) {
                // Connexion réussie, redirige vers l'écran principal
                navigateToHome()
            } else {
                // Affiche le message d'erreur
                errorMessageTextView.text = "Nom d'utilisateur ou mot de passe incorrect"
            }
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()  // Ferme l'activité de connexion
    }
}
