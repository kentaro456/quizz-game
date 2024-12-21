package com.example.project_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

data class ImageItem(
    val titles: List<String>,
    val resourceId: Int,
    val series: String
)

class ImagesQuizActivity : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var scoreTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var seriesHintTextView: TextView

    private val gameState = MutableLiveData<GameState>()
    private var usedImages = mutableSetOf<Int>()

    private data class GameState(
        var score: Int = 0,
        var incorrectAnswers: Int = 0,
        var currentImageIndex: Int = -1,
        var hintsUsed: Int = 0
    )

    private val images = listOf(
        ImageItem(listOf("L", "Light", "Kenny", "Ryuk"), R.drawable.l_death_note, "Death Note"),
        ImageItem(listOf("Livail", "Eren", "Mikasa", "Reiner"), R.drawable.livail, "Attack on Titan"),
        ImageItem(listOf("Ciel", "Sebastian", "Grell", "Undertaker"), R.drawable.ciel_phantomhive_kuroshitsuji, "Black Butler"),
        ImageItem(listOf("Rem", "Emilia", "Crusch", "Reid"), R.drawable.rem_rezero, "Re:Zero"),
        ImageItem(listOf("Asuna", "Kirito", "Leafa", "Suguha"), R.drawable.asuna_sword_art_online, "Sword Art Online"),
        ImageItem(listOf("Eikichi", "Onizuka", "Fujimoto", "Takahashi"), R.drawable.eikichi_onizuka_oreat_teacher_onizuka, "Great Teacher Onizuka"),
        ImageItem(listOf("Erza", "Natsu", "Lucy", "Gray"), R.drawable.erza_scarlett_fairy_tail, "Fairy Tail"),
        ImageItem(listOf("Naruto", "Sasuke", "Kakashi", "Sakura"), R.drawable.uzumaki_naruto_naruto, "Naruto"),
        ImageItem(listOf("Grey", "Natsu", "Lucy", "Wendy"), R.drawable.grey_fullbuster_fairy_tail, "Fairy Tail"),
        ImageItem(listOf("Sakura", "Hinata", "Ino", "Temari"), R.drawable.haruno_sakura_naruto, "Naruto"),
        ImageItem(listOf("Hinata", "Sakura", "Ino", "Tsunade"), R.drawable.hinata_hyuuga_naruto, "Naruto"),
        ImageItem(listOf("Hitsugaya", "Ichigo", "Renji", "Yoruichi"), R.drawable.hitsugaya_t_333_shir_bleach, "Bleach"),
        ImageItem(listOf("Yato", "Hiyori", "Bishamon", "Yukine"), R.drawable.yato_noragami, "Noragami"),
        ImageItem(listOf("Itachi", "Sasuke", "Madara", "Kage"), R.drawable.itachi_uchiwa_naruto, "Naruto"),
        ImageItem(listOf("Kakashi", "Naruto", "Sasuke", "Iruka"), R.drawable.kakashi_hatake_naruto, "Naruto"),
        ImageItem(listOf("Ulquiorra", "Grimmjow", "Ichigo", "Nelliel"), R.drawable.ulquiorra_schiffer_bleach, "Bleach"),
        ImageItem(listOf("Gaara", "Kankuro", "Temari", "Sasori"), R.drawable.sabaku_no_gaara_naruto, "Naruto"),
        ImageItem(listOf("Ichigo", "Rukia", "Orihime", "Uryu"), R.drawable.kurosaki_ichigo_bleach, "Bleach"),
        ImageItem(listOf("Mikasa", "Armin", "Jean", "Levi"), R.drawable.mikasa_ackerman_snk, "Attack on Titan"),
        ImageItem(listOf("Ace", "Luffy", "Sabo", "Garp"), R.drawable.portgas_d_ace_one_piece, "One Piece"),
        ImageItem(listOf("Nezuko", "Tanjiro", "Zenitsu", "Inosuke"), R.drawable.kamado_nezuko_kimetsu_no_yaiba, "Demon Slayer"),
        ImageItem(listOf("Sebastian", "Ciel", "Grell", "William"), R.drawable.sebastian_michaelis_kuroshitsuji, "Black Butler"),
        ImageItem(listOf("Kirito", "Asuna", "Leafa", "Yui"), R.drawable.kirito_sword_art_online, "Sword Art Online")
    )

    companion object {
        const val MAX_SCORE = 10
        const val MAX_INCORRECT = 3
        const val POINTS_PER_CORRECT = 1
        const val MAX_HINTS = 3
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_quiz)

        initializeViews()
        setupGameStateObserver()
        setupClickListeners()

        gameState.value = GameState()
        loadQuestionAndOptions()
    }

    private fun initializeViews() {
        questionTextView = findViewById(R.id.questionTextView)
        imageView = findViewById(R.id.imageView)
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup)
        submitButton = findViewById(R.id.submitButton)
        scoreTextView = findViewById(R.id.scoreTextView)
        progressBar = findViewById(R.id.progressBar)
        seriesHintTextView = findViewById(R.id.seriesHintTextView)

        // Initialize progress bar
        progressBar.max = MAX_SCORE
        progressBar.progress = 0
    }

    @SuppressLint("StringFormatInvalid")
    private fun setupGameStateObserver() {
        gameState.observe(this) { state ->
            scoreTextView.text = getString(R.string.score_format, state.score)
            progressBar.progress = state.score
        }
    }

    private fun setupClickListeners() {
        submitButton.setOnClickListener { checkAnswer() }


        findViewById<Button>(R.id.hintButton).setOnClickListener {
            showHint()
        }
    }

    private fun loadQuestionAndOptions() {
        gameState.value?.let { state ->
            // Select a random unused image
            val availableIndices = images.indices.toSet() - usedImages
            if (availableIndices.isEmpty()) {
                usedImages.clear() // Reset if all images have been used
            }

            state.currentImageIndex = availableIndices.random()
            usedImages.add(state.currentImageIndex)

            val currentImage = images[state.currentImageIndex]

            // Update UI
            questionTextView.text = getString(R.string.question_text)
            imageView.setImageResource(currentImage.resourceId)
            seriesHintTextView.visibility = View.INVISIBLE

            setupOptions(currentImage)
        }
    }

    private fun setupOptions(currentImage: ImageItem) {
        val correctOption = currentImage.titles.first()
        val allOptions = generateOptions(correctOption)

        optionsRadioGroup.removeAllViews()
        allOptions.forEach { option ->
            RadioButton(this).apply {
                text = option
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )
                optionsRadioGroup.addView(this)
            }
        }
    }

    private fun generateOptions(correctOption: String): List<String> {
        val incorrectOptions = mutableSetOf<String>()
        while (incorrectOptions.size < 3) {
            val randomImage = images.random()
            val randomOption = randomImage.titles.random()
            if (randomOption != correctOption) {
                incorrectOptions.add(randomOption)
            }
        }
        return (incorrectOptions + correctOption).shuffled()
    }

    private fun showHint() {
        gameState.value?.let { state ->
            if (state.hintsUsed < MAX_HINTS) {
                val currentImage = images[state.currentImageIndex]
                seriesHintTextView.text = getString(R.string.hint_format, currentImage.series)
                seriesHintTextView.visibility = View.VISIBLE
                state.hintsUsed++
                gameState.value = state
            } else {
                Toast.makeText(this, getString(R.string.no_hints_remaining), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAnswer() {
        val selectedOptionId = optionsRadioGroup.checkedRadioButtonId
        if (selectedOptionId == -1) {
            Toast.makeText(this, getString(R.string.select_option), Toast.LENGTH_SHORT).show()
            return
        }

        gameState.value?.let { state ->
            val selectedOption = findViewById<RadioButton>(selectedOptionId).text.toString()
            val currentImage = images[state.currentImageIndex]

            if (selectedOption == currentImage.titles.first()) {
                handleCorrectAnswer(state)
            } else {
                handleIncorrectAnswer(state, currentImage.titles.first())
            }
        }
    }

    private fun handleCorrectAnswer(state: GameState) {
        state.score += POINTS_PER_CORRECT
        showAnswerDialog(true)

        if (state.score >= MAX_SCORE) {
            endGame(getString(R.string.win_message))
        } else {
            gameState.value = state
            loadQuestionAndOptions()
        }
    }

    private fun handleIncorrectAnswer(state: GameState, correctAnswer: String) {
        state.incorrectAnswers++
        showAnswerDialog(false, correctAnswer)

        if (state.incorrectAnswers >= MAX_INCORRECT) {
            endGame(getString(R.string.game_over_message))
        } else {
            gameState.value = state
            loadQuestionAndOptions()
        }
    }

    private fun showAnswerDialog(isCorrect: Boolean, correctAnswer: String = "") {
        AlertDialog.Builder(this)
            .setTitle(if (isCorrect) getString(R.string.correct) else getString(R.string.incorrect))
            .setMessage(if (isCorrect)
                getString(R.string.correct_message)
            else getString(R.string.incorrect_message, correctAnswer))
            .setPositiveButton(getString(R.string.continue_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun endGame(message: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.game_ended))
            .setMessage(message)
            .setPositiveButton(getString(R.string.return_home)) { _, _ ->
                goToHome()
            }
            .setCancelable(false)
            .show()
    }

    private fun goToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}