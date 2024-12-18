package com.example.project_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

data class ImageItem(val titles: List<String>, val resourceId: Int)

class ImagesQuizActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var imageView: ImageView
    private lateinit var optionsRadioGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var scoreTextView: TextView
    private var currentImageIndex = 0
    private var score = 0
    private var incorrectAnswers = 0 // Count incorrect answers

    private val images = listOf(
        ImageItem(listOf("L", "light","kenny"), R.drawable.l_death_note),
        ImageItem(listOf("Image 2", "Option 2"), R.drawable.livail), // attaque des titans livail
        ImageItem(listOf("Image 3", "Option 3"), R.drawable.ciel_phantomhive_kuroshitsuji),
        ImageItem(listOf("Image 4", "Option 4"), R.drawable.rem_rezero),
        ImageItem(listOf("Image 5", "Option 5"), R.drawable.asuna_sword_art_online),
        ImageItem(listOf("Image 6", "Option 6"), R.drawable.eikichi_onizuka_oreat_teacher_onizuka),
        ImageItem(listOf("Image 7", "Option 7"), R.drawable.erza_scarlett_fairy_tail),
        ImageItem(listOf("Image 8", "Option 8"), R.drawable.uzumaki_naruto_naruto),
        ImageItem(listOf("Image 9", "Option 9"), R.drawable.grey_fullbuster_fairy_tail),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.haruno_sakura_naruto),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.hinata_hyuuga_naruto),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.hitsugaya_t_333_shir_bleach),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.yato_noragami),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.itachi_uchiwa_naruto),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.kakashi_hatake_naruto),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.ulquiorra_schiffer_bleach),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.sabaku_no_gaara_naruto),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.kurosaki_ichigo_bleach),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.mikasa_ackerman_snk),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.portgas_d_ace_one_piece),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.kamado_nezuko_kimetsu_no_yaiba),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.sebastian_michaelis_kuroshitsuji),
        ImageItem(listOf("Image 10", "Option 10"), R.drawable.kirito_sword_art_online)
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_quiz)

        // Initialize views
        questionTextView = findViewById(R.id.questionTextView)
        imageView = findViewById(R.id.imageView)
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup)
        submitButton = findViewById(R.id.submitButton)
        scoreTextView = findViewById(R.id.scoreTextView)

        // Set up button click listeners
        submitButton.setOnClickListener { checkAnswer() }

        // Load the first question
        loadQuestionAndOptions()
    }

    private fun loadQuestionAndOptions() {
        // Choose a random image
        currentImageIndex = Random.nextInt(images.size)
        val currentImage = images[currentImageIndex]

        // Set the question text
        questionTextView.text = "What is this image?"

        // Set the image
        imageView.setImageResource(currentImage.resourceId)

        // Set the options (include the correct answer and random incorrect ones)
        val correctOption = currentImage.titles.random()
        val options = mutableListOf(correctOption)
        while (options.size < 4) {
            val randomOption = images[Random.nextInt(images.size)].titles.random()
            if (randomOption !in options) options.add(randomOption)
        }
        options.shuffle()

        // Populate the radio buttons with options
        for (i in options.indices) {
            val radioButton = optionsRadioGroup.getChildAt(i) as? RadioButton
            radioButton?.text = options[i]
        }
    }

    private fun checkAnswer() {
        val selectedOptionId = optionsRadioGroup.checkedRadioButtonId
        val selectedOption = findViewById<RadioButton>(selectedOptionId)?.text.toString()
        val correctOptions = images[currentImageIndex].titles

        if (selectedOption in correctOptions) {
            score++
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            incorrectAnswers++
            Toast.makeText(this, "Incorrect! The correct answer was: ${correctOptions.first()}", Toast.LENGTH_SHORT).show()

            // Check if incorrect answers exceed 3
            if (incorrectAnswers >= 3) {
                Toast.makeText(this, "Game Over! Returning to Home.", Toast.LENGTH_LONG).show()
                goToHome() // Navigate to HomeActivity
                return
            }
        }

        // Update the score
        scoreTextView.text = "Score: $score"

        // Load the next question or end the game if score >= 10
        if (score < 10) {
            loadQuestionAndOptions()
        } else {
            Toast.makeText(this, "You won!", Toast.LENGTH_LONG).show()
            goToHome() // Navigate to HomeActivity if user wins
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish() // End the current activity
    }
}
