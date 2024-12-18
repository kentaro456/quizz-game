package com.example.project_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.example.project_android.SeriesActivity
import com.example.project_android.SportsActivity


class HomeActivity : AppCompatActivity() {

    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    //<item name="androidx_compose_ui_view_composition_context" type="id"/>
        val filmsButton: Button = findViewById(R.id.filmsButton)
        val seriesButton: Button = findViewById(R.id.seriesButton)
        val animesButton: Button = findViewById(R.id.animesButton)
        val sportsButton: Button = findViewById(R.id.sportsButton)
        val MusicButton: Button = findViewById(R.id.MusicButton)

        val quizButton: Button = findViewById(R.id.quizButton)

        filmsButton.setOnClickListener {
            selectedCategory = "Films"
            Toast.makeText(this, "Catégorie Films sélectionnée", Toast.LENGTH_SHORT).show()
        }

        seriesButton.setOnClickListener {
            selectedCategory = "Séries"
            Toast.makeText(this, "Catégorie Séries sélectionnée", Toast.LENGTH_SHORT).show()
        }

        animesButton.setOnClickListener {
            selectedCategory = "Animés"
            Toast.makeText(this, "Catégorie Animés sélectionnée", Toast.LENGTH_SHORT).show()
        }

        sportsButton.setOnClickListener {
            selectedCategory = "Sports"
            Toast.makeText(this, "Catégorie Sports sélectionnée", Toast.LENGTH_SHORT).show()
        }
        MusicButton.setOnClickListener {
            selectedCategory = "Music"
            Toast.makeText(this, "Catégorie Music sélectionnée", Toast.LENGTH_SHORT).show()
        }

        quizButton.setOnClickListener {
            if (selectedCategory != null) {
                when (selectedCategory) {
                    "Films" -> startActivity(Intent(this, FilmActivity::class.java))
                   "Séries" -> startActivity(Intent(this, SeriesActivity::class.java))
                    "Animés" -> startActivity(Intent(this, QuizActivity::class.java))
                     "Sports" -> startActivity(Intent(this, SportsActivity::class.java))
                    "Music" -> startActivity(Intent(this, MusicQuizActivity::class.java))
                }
            } else {
                Toast.makeText(this, "Veuillez sélectionner une catégorie", Toast.LENGTH_SHORT).show()
            }
        }
    }
}