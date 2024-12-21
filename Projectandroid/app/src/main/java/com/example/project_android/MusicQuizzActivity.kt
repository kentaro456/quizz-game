package com.example.project_android

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

data class Song(
    val correctTitle: String,       // Titre correct de la chanson
    val resourceId: Int,           // ID de la ressource audio
    val category: String,          // Catégorie de l'anime
    val difficulty: String = "Normal" // Difficulté de la question (optionnel)
)

class MusicQuizActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var playButton: Button
    private lateinit var pauseButton: Button
    private lateinit var homeButton: Button
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var scoreTextView: TextView
    private lateinit var progressTextView: TextView
    private var mediaPlayer: MediaPlayer? = null
    private var currentSongIndex = 0
    private var score = 0
    private var incorrectAnswers = 0
    private val playedSongs = mutableSetOf<Int>()
    private var isSongPlaying = false
    private var isAnswered = false

    // Liste complète des chansons
    private val songs = listOf(
        Song("Attack on Titan Opening", R.raw.attack_on_titan_opening, "Action", "Difficile"),
        Song("Attack on Titan Season 2 Opening", R.raw.attack_on_titan_season_2_opening, "Action", "Difficile"),
        Song("Black Clover Opening 10", R.raw.black_clover_opening_10, "Action", "Normal"),
        Song("Boku no Hero Academia Opening 3", R.raw.boku_no_hero_academia_opening_3, "Action", "Facile"),
        Song("Death Parade Opening", R.raw.death_parade_opening, "Supernatural", "Normal"),
        Song("Fairy Tail Opening 1", R.raw.fairy_tail_opening_1, "Fantasy", "Facile"),
        Song("Fairy Tail Opening 2", R.raw.fairy_tail_opening_2, "Fantasy", "Normal"),
        Song("Fire Force Opening", R.raw.fire_force_opening, "Action", "Normal"),
        Song("Gintama Opening 13", R.raw.gintama_opening_13, "Comedy", "Difficile"),
        Song("Hiiro no Kakera Opening 1", R.raw.hiiro_no_kakera_opening_1, "Romance", "Difficile"),
        Song("Nanatsu no Taizai Opening 1", R.raw.nanatsu_no_taizai_opening_1, "Fantasy", "Normal"),
        Song("Naruto Ending 1", R.raw.naruto_ending_1, "Action", "Facile"),
        Song("Naruto Shippuden Opening 3", R.raw.naruto_shippuden_opening_3, "Action", "Facile"),
        Song("Naruto Shippuden Opening 6", R.raw.naruto_shippuden_opening_6, "Action", "Normal"),
        Song("Noragami Opening 2", R.raw.noragami_opening_2, "Supernatural", "Normal"),
        Song("Samurai Champloo Opening", R.raw.samurai_champloo_opening, "Action", "Difficile"),
        Song("Beastars Opening", R.raw.beastars_opening, "Drama", "Normal"),
        Song("To Your Eternity Opening", R.raw.to_your_eternity_opening, "Fantasy", "Difficile"),
        Song("Fullmetal Alchemist Brotherhood Opening", R.raw.fullmetal_alchemist_brotherhood_opening, "Action", "Facile"),
        Song("Btoom Opening 1", R.raw.btoom_opening_1, "Action", "Difficile"),
        Song("Tokyo Ghoul Opening", R.raw.tokyo_ghoul_opening, "Horror", "Facile"),
        Song("Minami Kawaki wo Ameku MV", R.raw.minami_kawaki_wo_ameku_mv, "Drama", "Normal")
    )

    // Alternatives par catégorie pour les mauvaises réponses
    private val alternativesByCategory = mapOf(
        "Action" to listOf(
            "One Piece Opening", "Naruto Opening", "Dragon Ball Z Opening", "My Hero Academia Opening",
            "Attack on Titan Opening", "Demon Slayer Opening", "Jujutsu Kaisen Opening", "Hunter x Hunter Opening",
            "One Punch Man Opening", "Mob Psycho 100 Opening", "Black Clover Opening", "Fire Force Opening"
        ),
        "Fantasy" to listOf(
            "Fairy Tail Opening", "The Seven Deadly Sins Opening", "Black Clover Opening",
            "Magi Opening", "Re:Zero Opening", "Sword Art Online Opening", "That Time I Got Reincarnated as a Slime Opening",
            "Overlord Opening", "No Game No Life Opening", "Log Horizon Opening"
        ),
        "Supernatural" to listOf(
            "Noragami Opening", "Death Note Opening", "Blue Exorcist Opening", "Soul Eater Opening",
            "Bleach Opening", "JoJo's Bizarre Adventure Opening", "Yu Yu Hakusho Opening",
            "Bungo Stray Dogs Opening", "Mob Psycho 100 Opening", "The God of High School Opening"
        ),
        "Horror" to listOf(
            "Tokyo Ghoul Opening", "Another Opening", "Hellsing Opening", "Parasyte Opening",
            "The Promised Neverland Opening", "Elfen Lied Opening", "Deadman Wonderland Opening",
            "Higurashi Opening", "Blood+ Opening", "Shiki Opening"
        ),
        "Drama" to listOf(
            "Your Lie in April Opening", "Anohana Opening", "Steins;Gate Opening", "Erased Opening",
            "Angel Beats Opening", "Violet Evergarden Opening", "March Comes in Like a Lion Opening",
            "A Silent Voice Opening", "Clannad Opening", "Orange Opening"
        ),
        "Comedy" to listOf(
            "Gintama Opening", "One Punch Man Opening", "K-On! Opening", "Nichijou Opening",
            "Grand Blue Opening", "Daily Lives of High School Boys Opening", "Konosuba Opening",
            "Saiki K. Opening", "Love is War Opening", "Asobi Asobase Opening"
        ),
        "Romance" to listOf(
            "Toradora Opening", "Kimi ni Todoke Opening", "Fruits Basket Opening", "Horimiya Opening",
            "Clannad Opening", "Your Name Opening", "Rascal Does Not Dream of Bunny Girl Senpai Opening",
            "Kaguya-sama: Love is War Opening", "Golden Time Opening", "My Love Story!! Opening"
        )
    )
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        // Initialisation des vues
        initializeViews()

        // Configuration des événements
        setupEventListeners()

        // Démarrer le quiz
        loadQuestionAndOptions()
    }

    private fun initializeViews() {
        questionTextView = findViewById(R.id.questionTextView)
        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)
        homeButton = findViewById(R.id.homeButton)
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup)
        submitButton = findViewById(R.id.submitButton)
        scoreTextView = findViewById(R.id.scoreTextView)
        progressTextView = findViewById(R.id.progressTextView)

        // Initialiser les textes
        scoreTextView.text = "Score : 0"
        progressTextView.text = "Question : 0/${songs.size}"
    }

    private fun setupEventListeners() {
        playButton.setOnClickListener { playSong() }
        pauseButton.setOnClickListener { pauseSong() }
        homeButton.setOnClickListener { goToHome() }
        submitButton.setOnClickListener { checkAnswer() }
    }

    private fun getRandomIncorrectOptions(currentSong: Song, count: Int): List<String> {
        val category = currentSong.category
        val alternatives = mutableListOf<String>()

        // Ajouter des alternatives de la même catégorie
        val categoryAlternatives = alternativesByCategory[category]?.filter {
            it != currentSong.correctTitle &&
                    it !in songs.map { song -> song.correctTitle }
        }

        if (!categoryAlternatives.isNullOrEmpty()) {
            alternatives.addAll(categoryAlternatives.shuffled().take(count / 2))
        }

        // Ajouter des alternatives d'autres catégories
        val otherCategories = alternativesByCategory.filterKeys { it != category }
        val otherOptions = otherCategories.values.flatten()
            .filter { it != currentSong.correctTitle && it !in songs.map { song -> song.correctTitle } }
            .shuffled()
            .take(count - alternatives.size)

        alternatives.addAll(otherOptions)
        return alternatives.shuffled().take(count)
    }

    private fun loadQuestionAndOptions() {
        // Vérifier si toutes les chansons ont été jouées
        if (playedSongs.size == songs.size) {
            Toast.makeText(this, "Félicitations ! Vous avez terminé le quiz !", Toast.LENGTH_LONG).show()
            resetGame()
            return
        }

        // Sélectionner une nouvelle chanson
        do {
            currentSongIndex = Random.nextInt(songs.size)
        } while (currentSongIndex in playedSongs)

        playedSongs.add(currentSongIndex)
        val currentSong = songs[currentSongIndex]

        // Mettre à jour l'interface
        questionTextView.text = "Quelle est cette chanson ? (${currentSong.difficulty})"
        progressTextView.text = "Question : ${playedSongs.size}/${songs.size}"

        // Générer les options
        val options = mutableListOf(currentSong.correctTitle)
        options.addAll(getRandomIncorrectOptions(currentSong, 3))
        options.shuffle()

        // Mettre à jour les boutons radio
        optionsRadioGroup.removeAllViews()
        options.forEach { optionText ->
            val radioButton = RadioButton(this).apply {
                text = optionText
                id = View.generateViewId()
                textSize = 16f
                setPadding(20, 20, 20, 20)
            }
            optionsRadioGroup.addView(radioButton)
        }

        // Réinitialiser les états
        isSongPlaying = false
        isAnswered = false
        optionsRadioGroup.clearCheck()
    }

    private fun playSong() {
        if (isSongPlaying) {
            Toast.makeText(this, "La chanson est déjà en cours de lecture", Toast.LENGTH_SHORT).show()
            return
        }

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, songs[currentSongIndex].resourceId)
        mediaPlayer?.setOnCompletionListener {
            stopSong()
        }
        mediaPlayer?.start()
        isSongPlaying = true
    }

    private fun pauseSong() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            isSongPlaying = false
        }
    }

    private fun stopSong() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
        isSongPlaying = false
    }

    private fun checkAnswer() {
        if (!isSongPlaying && mediaPlayer == null) {
            Toast.makeText(this, "Veuillez d'abord écouter la chanson", Toast.LENGTH_SHORT).show()
            return
        }

        if (isAnswered) {
            Toast.makeText(this, "Vous avez déjà répondu à cette question", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedOptionId = optionsRadioGroup.checkedRadioButtonId
        if (selectedOptionId == -1) {
            Toast.makeText(this, "Veuillez sélectionner une réponse", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedOption = findViewById<RadioButton>(selectedOptionId)?.text.toString()
        val correctSong = songs[currentSongIndex]

        if (selectedOption == correctSong.correctTitle) {
            score++
            val bonus = when(correctSong.difficulty) {
                "Difficile" -> 3
                "Normal" -> 2
                else -> 1
            }
            score += (bonus - 1)
            Toast.makeText(this, "Correct ! +$bonus points (${correctSong.difficulty})", Toast.LENGTH_SHORT).show()
        } else {
            incorrectAnswers++
            Toast.makeText(this,
                "Incorrect ! La bonne réponse était : ${correctSong.correctTitle}\n" +
                        "Erreurs restantes : ${3 - incorrectAnswers}",
                Toast.LENGTH_LONG
            ).show()

            if (incorrectAnswers >= 3) {
                Toast.makeText(this, "Game Over ! Score final : $score", Toast.LENGTH_LONG).show()
                goToHome()
                return
            }
        }

        scoreTextView.text = "Score : $score"
        stopSong()
        isAnswered = true

        // Charger la prochaine question après un court délai
        submitButton.postDelayed({
            loadQuestionAndOptions()
        }, 1500)
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("FINAL_SCORE", score)
        startActivity(intent)
        finish()
    }

    private fun resetGame() {
        score = 0
        incorrectAnswers = 0
        playedSongs.clear()
        scoreTextView.text = "Score : $score"
        loadQuestionAndOptions()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopSong()
    }
}