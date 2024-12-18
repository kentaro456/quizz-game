package com.example.project_android

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

// Classe représentant une chanson avec ses titres possibles et son identifiant de ressource
data class Song(val titles: List<String>, val resourceId: Int)

class MusicQuizActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private lateinit var homeButton: Button // Bouton pour retourner à la page d'accueil
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var scoreTextView: TextView
    private var mediaPlayer: MediaPlayer? = null
    private var currentSongIndex = 0
    private var score = 0
    private var incorrectAnswers = 0 // Compteur pour les mauvaises réponses
    private val playedSongs = mutableSetOf<Int>() // Ensemble pour stocker les chansons déjà jouées

    // Liste des chansons avec leurs titres et identifiants
    private val songs = listOf(
        Song(listOf("Attack on Titan Opening", "Shingeki no Kyojin Opening"), R.raw.attack_on_titan_opening),
        Song(listOf("Attack on Titan Season 2 Opening"), R.raw.attack_on_titan_season_2_opening),
        Song(listOf("Black Clover Opening 10"), R.raw.black_clover_opening_10),
        Song(listOf("Boku no Hero Academia Opening 3", "My Hero Academia Opening 3"), R.raw.boku_no_hero_academia_opening_3),
        Song(listOf("Death Parade Opening", "Flyers"), R.raw.death_parade_opening),
        Song(listOf("Fairy Tail Opening 1"), R.raw.fairy_tail_opening_1),
        Song(listOf("Naruto Shippuden Opening 6"), R.raw.naruto_shippuden_opening_6),
        Song(listOf("Noragami Opening 2", "Kyouran Hey Kids!!"), R.raw.noragami_opening_2),
        Song(listOf("Beastars Opening"), R.raw.beastars_opening),
        Song(listOf("Fullmetal Alchemist Brotherhood Opening"), R.raw.fullmetal_alchemist_brotherhood_opening)
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        // Initialisation des vues
        questionTextView = findViewById(R.id.questionTextView)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        homeButton = findViewById(R.id.homeButton) // Bouton pour retourner à l'accueil
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup)
        submitButton = findViewById(R.id.submitButton)
        scoreTextView = findViewById(R.id.scoreTextView)

        // Configuration des actions sur les boutons
        playButton.setOnClickListener { playSong() }
        pauseButton.setOnClickListener { pauseSong() }
        homeButton.setOnClickListener { goToHome() } // Retour à la page d'accueil
        submitButton.setOnClickListener { checkAnswer() }

        // Charger la première question
        loadQuestionAndOptions()
    }

    private fun loadQuestionAndOptions() {
        // Vérifier si toutes les chansons ont été jouées
        if (playedSongs.size == songs.size) {
            Toast.makeText(this, "Toutes les chansons ont été jouées !", Toast.LENGTH_LONG).show()
            goToHome() // Terminer le jeu ou retourner à l'accueil
            return
        }

        // Sélectionner une chanson qui n'a pas encore été jouée
        do {
            currentSongIndex = Random.nextInt(songs.size)
        } while (currentSongIndex in playedSongs)

        // Ajouter cette chanson à la liste des chansons jouées
        playedSongs.add(currentSongIndex)

        val currentSong = songs[currentSongIndex]

        // Définir le texte de la question
        questionTextView.text = "Quelle est cette chanson ?"

        // Préparer les options (réponse correcte et réponses incorrectes aléatoires)
        val correctOption = currentSong.titles.random()
        val options = mutableListOf(correctOption)
        while (options.size < 4) {
            val randomOption = songs[Random.nextInt(songs.size)].titles.random()
            if (randomOption !in options) options.add(randomOption)
        }
        options.shuffle()

        // Afficher les options dans le groupe de boutons radio
        for (i in options.indices) {
            val radioButton = optionsRadioGroup.getChildAt(i) as? RadioButton
            radioButton?.text = options[i]
        }
    }

    private fun playSong() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex].resourceId)
            mediaPlayer?.setOnCompletionListener { stopSong() }
        }
        mediaPlayer?.start() // Démarrer la lecture de la chanson
    }

    private fun pauseSong() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause() // Mettre en pause la lecture de la chanson
        }
    }

    private fun stopSong() {
        // Arrêter la chanson et libérer les ressources
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }

    private fun checkAnswer() {
        val selectedOptionId = optionsRadioGroup.checkedRadioButtonId
        val selectedOption = findViewById<RadioButton>(selectedOptionId)?.text.toString()
        val correctOptions = songs[currentSongIndex].titles

        // Vérifier si la réponse est correcte
        if (selectedOption in correctOptions) {
            score++
            Toast.makeText(this, "Correct !", Toast.LENGTH_SHORT).show()
        } else {
            incorrectAnswers++
            Toast.makeText(this, "Incorrect !", Toast.LENGTH_SHORT).show()

            // Vérifier si l'utilisateur a fait trop d'erreurs
            if (incorrectAnswers >= 3) {
                Toast.makeText(this, "Game Over ! Retour à l'accueil.", Toast.LENGTH_LONG).show()
                goToHome()
                return
            }
        }

        // Mettre à jour le score
        scoreTextView.text = "Score : $score"

        // Arrêter la chanson actuelle
        stopSong()

        // Charger la question suivante ou afficher un message de victoire
        if (score < 10) {
            loadQuestionAndOptions()
            playSong()
        } else {
            Toast.makeText(this, "Vous avez gagné !", Toast.LENGTH_LONG).show()
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent) // Retour à l'activité d'accueil
        finish() // Terminer l'activité actuelle
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSong() // Nettoyer le lecteur média
    }
}
