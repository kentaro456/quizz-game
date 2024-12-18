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
    private var isSongPlaying = false // Indicateur pour savoir si une chanson est en cours de lecture
    private var isAnswered = false // Indicateur pour savoir si une question a été répondue

    // Liste des chansons avec leurs titres et identifiants
    private val songs = listOf(
        Song(listOf(
            "Attack on Titan Opening", "Shingeki no Kyojin Opening", "Guren no Yumiya", "The Reluctant Heroes", "Jiyuu no Tsubasa"
        ), R.raw.attack_on_titan_opening),
        Song(listOf(
            "Attack on Titan Season 2 Opening", "Shingeki no Kyojin Season 2 Opening", "Red Swan", "Shingeki no Kyojin Opening 2"
        ), R.raw.attack_on_titan_season_2_opening),
        Song(listOf(
            "Black Clover Opening 10", "Black Clover Opening 10 – Wild", "Black Clover Opening 10 Theme", "Black Clover OP10"
        ), R.raw.black_clover_opening_10),
        Song(listOf(
            "Boku no Hero Academia Opening 3", "My Hero Academia Opening 3", "Koushiki Hero", "The Day", "Boku no Hero Academia 3 OP"
        ), R.raw.boku_no_hero_academia_opening_3),
        Song(listOf(
            "Death Parade Opening", "Flyers", "Flyers Death Parade OP", "Flyers Opening"
        ), R.raw.death_parade_opening),
        Song(listOf(
            "Fairy Tail Opening 1", "Fairy Tail OP1", "Snow Fairy", "Fairy Tail Theme Song"
        ), R.raw.fairy_tail_opening_1),
        Song(listOf(
            "Fairy Tail Opening 2", "Fairy Tail OP2", "S.O.W. Sense of Wonder", "Sense of Wonder", "Fairy Tail 2 OP"
        ), R.raw.fairy_tail_opening_2),
        Song(listOf(
            "Fire Force Opening", "Inferno", "En'en", "Fire Force OP", "Inferno Fire Force"
        ), R.raw.fire_force_opening),
        Song(listOf(
            "Gintama Opening 13", "Gintama OP13", "Let Me Hear", "Let Me Hear Gintama OP", "Gintama 13 Opening"
        ), R.raw.gintama_opening_13),
        Song(listOf(
            "Hiiro no Kakera Opening 1", "Hiiro no Kakera OP1", "Kimi to no Yakusoku", "Promise with You"
        ), R.raw.hiiro_no_kakera_opening_1),
        Song(listOf(
            "Nanatsu no Taizai Opening 1", "Seven Deadly Sins Opening 1", "Nakama", "The Seven Deadly Sins OP1", "Seven Deadly Sins Theme"
        ), R.raw.nanatsu_no_taizai_opening_1),
        Song(listOf(
            "Naruto Ending 1", "Naruto ED1", "Wind", "Naruto Wind Theme"
        ), R.raw.naruto_ending_1),
        Song(listOf(
            "Naruto Shippuden Opening 3", "Naruto Shippuden OP3", "Blue Bird", "The Blue Bird", "Naruto Shippuden 3 OP"
        ), R.raw.naruto_shippuden_opening_3),
        Song(listOf(
            "Naruto Shippuden Opening 6", "Naruto Shippuden OP6", "Sign", "Sign Naruto OP6", "Naruto 6 Opening"
        ), R.raw.naruto_shippuden_opening_6),
        Song(listOf(
            "Noragami Opening 2", "Kyouran Hey Kids!!", "Hey Kids!!", "Noragami 2 OP", "Noragami Opening Theme"
        ), R.raw.noragami_opening_2),
        Song(listOf(
            "Samurai Champloo Opening", "Battlecry", "Samurai Champloo OP", "Samurai Champloo Theme"
        ), R.raw.samurai_champloo_opening),
        Song(listOf(
            "Beastars Opening", "Wild Side", "Beastars OP", "Wild Side Opening"
        ), R.raw.beastars_opening),
        Song(listOf(
            "To Your Eternity Opening", "Do Your Best", "Fumetsu no Anata e OP", "To Your Eternity Theme"
        ), R.raw.to_your_eternity_opening),
        Song(listOf(
            "Fullmetal Alchemist Brotherhood Opening", "Again", "Fullmetal Alchemist OP1", "Fullmetal Alchemist Theme"
        ), R.raw.fullmetal_alchemist_brotherhood_opening),
        Song(listOf(
            "Btoom Opening 1", "Btoom OP1", "No Fear", "Btoom Theme"
        ), R.raw.btoom_opening_1),
        Song(listOf(
            "Opening 1", "OP1", "Opening Song 1", "Anime Opening 1"
        ), R.raw.attack_on_titan_opening),
        Song(listOf(
            "Orb Opening", "Kaiju OP", "Sakanaction Opening", "Kaiju Theme"
        ), R.raw.kaiju_sakanaction_opening),
        Song(listOf(
            "Tokyo Ghoul Opening", "Unravel", "Tokyo Ghoul OP1", "Unravel Tokyo Ghoul"
        ), R.raw.tokyo_ghoul_opening),
        Song(listOf(
            "Minami Kawaki wo Ameku MV", "Minami Kawaki wo Ameku", "Kawaki wo Ameku", "Minami Kawaki Opening"
        ), R.raw.minami_kawaki_wo_ameku_mv)
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        // Initialisation des vues
        questionTextView = findViewById(R.id.questionTextView)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        homeButton = findViewById(R.id.homeButton) // Bouton pour retourner à la page d'accueil
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

    // Fonction pour charger la question et les options
    private fun loadQuestionAndOptions() {
        // Vérifier si toutes les chansons ont été jouées
        if (playedSongs.size == songs.size) {
            Toast.makeText(this, "Toutes les chansons ont été jouées !", Toast.LENGTH_LONG).show()
            resetGame() // Réinitialiser le jeu
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

        // Ajouter des options incorrectes jusqu'à avoir 4 options
        while (options.size < 4) {
            val randomSong = songs[Random.nextInt(songs.size)]
            val randomOption = randomSong.titles.random()
            if (randomOption !in options) {
                options.add(randomOption)
            }
        }

        // Mélanger les options pour que la réponse correcte apparaisse à une position aléatoire
        options.shuffle()

        // Afficher les options dans le groupe de boutons radio
        for (i in options.indices) {
            val radioButton = optionsRadioGroup.getChildAt(i) as? RadioButton
            radioButton?.text = options[i]
        }

        // Réinitialiser les indicateurs
        isSongPlaying = false
        isAnswered = false
    }

    // Fonction pour lire la chanson
    private fun playSong() {
        if (isSongPlaying) {
            Toast.makeText(this, "La chanson est déjà en cours de lecture.", Toast.LENGTH_SHORT).show()
            return
        }

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex].resourceId)
            mediaPlayer?.setOnCompletionListener {
                stopSong()
            }
        }
        mediaPlayer?.start() // Démarrer la lecture de la chanson
        isSongPlaying = true
    }

    // Fonction pour mettre en pause la chanson
    private fun pauseSong() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause() // Mettre en pause la lecture de la chanson
            isSongPlaying = false
        }
    }

    // Fonction pour arrêter la chanson
    private fun stopSong() {
        // Arrêter la chanson et libérer les ressources
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        isSongPlaying = false
    }

    // Vérifier si la réponse sélectionnée est correcte
    private fun checkAnswer() {
        // Ne pas permettre de répondre si la chanson n'a pas encore été jouée
        if (!isSongPlaying) {
            Toast.makeText(this, "Vous devez d'abord écouter la chanson.", Toast.LENGTH_SHORT).show()
            return
        }

        // Empêcher de répondre si la question a déjà été répondue
        if (isAnswered) {
            Toast.makeText(this, "Vous avez déjà répondu à cette question.", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedOptionId = optionsRadioGroup.checkedRadioButtonId
        val selectedOption = findViewById<RadioButton>(selectedOptionId)?.text.toString()
        val correctOptions = songs[currentSongIndex].titles

        // Vérifier si la réponse est correcte
        if (selectedOption in correctOptions) {
            score++
            Toast.makeText(this, "Correct !", Toast.LENGTH_SHORT).show()
        } else {
            incorrectAnswers++
            Toast.makeText(this, "Incorrect ! La bonne réponse était : ${correctOptions.first()} (Mauvaises réponses : $incorrectAnswers)", Toast.LENGTH_SHORT).show()

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

        // Marquer la question comme répondue
        isAnswered = true

        // Charger la question suivante ou afficher un message de victoire
        if (score < 10) {
            loadQuestionAndOptions()
            playSong()
        } else {
            Toast.makeText(this, "Vous avez gagné !", Toast.LENGTH_LONG).show()
            goToHome() // Réinitialiser le jeu après la victoire
        }
    }

    // Fonction pour retourner à la page d'accueil
    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent) // Retour à l'activité d'accueil
        finish() // Terminer l'activité en cours
    }

    // Fonction pour réinitialiser le jeu
    private fun resetGame() {
        score = 0
        incorrectAnswers = 0
        playedSongs.clear() // Réinitialiser les chansons jouées
        scoreTextView.text = "Score : $score" // Réinitialiser le score
        loadQuestionAndOptions() // Recharger la question et les options
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSong() // Nettoyer le lecteur média
    }
}