package com.example.project_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

data class Question(val text: String, val answers: List<String>, val correctAnswer: String)

class QuizGame(private val questions: List<Question>) {
    private var currentQuestionIndex = 0
    var score = 0
    var wrongAnswers = 0

    fun getCurrentQuestion(): Question {
        return questions[currentQuestionIndex]
    }

    fun checkAnswer(userAnswer: String): Boolean {
        val currentQuestion = getCurrentQuestion()
        return userAnswer.trim().equals(currentQuestion.correctAnswer, ignoreCase = true)
    }

    fun nextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
    }

    fun restartGame() {
        score = 0
        wrongAnswers = 0
        currentQuestionIndex = 0
    }

    fun isGameOver(): Boolean {
        return score >= 10 || wrongAnswers >= 3
    }

    fun getGameStatus(): String {
        return when {
            score >= 10 -> "Félicitations, vous avez gagné !"
            wrongAnswers >= 3 -> "Désolé, vous avez perdu !"
            else -> "Continuez à jouer !"
        }
    }
}

class QuizActivity : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var answerButton1: Button
    private lateinit var answerButton2: Button
    private lateinit var answerButton3: Button
    private lateinit var answerButton4: Button
    private lateinit var scoreTextView: TextView
    private lateinit var buttonReturn: Button

    private val gameState = MutableLiveData<GameState>()
    private var usedQuestions = mutableSetOf<Int>()

    private data class GameState(
        var score: Int = 0,
        var incorrectAnswers: Int = 0,
        var currentQuestionIndex: Int = -1
    )

    private val questions = listOf(
        // Naruto
        Question("Qui est le personnage principal de Naruto ?", listOf("Naruto Uzumaki", "Sasuke Uchiha", "Kakashi Hatake", "Jiraiya"), "Naruto Uzumaki"),
        Question("Quel est le nom du démon renard à neuf queues scellé dans Naruto ?", listOf("Kurama", "Shukaku", "Gyuki", "Matatabi"), "Kurama"),
        Question("Qui est l'auteur de Naruto ?", listOf("Masashi Kishimoto", "Eiichiro Oda", "Tite Kubo", "Yoshihiro Togashi"), "Masashi Kishimoto"),

        // One Piece
        Question("Quel est le rêve de Monkey D. Luffy ?", listOf("Devenir le roi des pirates", "Trouver All Blue", "Maîtriser toutes les épées", "Détruire la marine"), "Devenir le roi des pirates"),
        Question("Quel est le fruit du démon mangé par Luffy ?", listOf("Gomu Gomu no Mi", "Mera Mera no Mi", "Hito Hito no Mi", "Suna Suna no Mi"), "Gomu Gomu no Mi"),
        Question("Qui est le capitaine des Pirates au Chapeau de Paille ?", listOf("Monkey D. Luffy", "Roronoa Zoro", "Sanji", "Nami"), "Monkey D. Luffy"),

        // Attack on Titan
        Question("Qui est le premier porteur du Titan Assaillant dans Attack on Titan ?", listOf("Eren Yeager", "Grisha Yeager", "Eren Kruger", "Zeke Yeager"), "Eren Kruger"),
        Question("Quel est le mur le plus extérieur dans Attack on Titan ?", listOf("Mur Maria", "Mur Rose", "Mur Sina", "Mur Paradis"), "Mur Maria"),
        Question("Qui a tué le Titan Colossal ?", listOf("Armin Arlert", "Eren Yeager", "Mikasa Ackerman", "Jean Kirstein"), "Armin Arlert"),

        // Demon Slayer
        Question("Qui est le créateur de Demon Slayer ?", listOf("Koyoharu Gotouge", "Hajime Isayama", "Masashi Kishimoto", "Tite Kubo"), "Koyoharu Gotouge"),
        Question("Quel est le nom de la sœur de Tanjiro ?", listOf("Nezuko", "Mitsuri", "Shinobu", "Kanae"), "Nezuko"),
        Question("Comment s'appelle la technique de respiration utilisée par Tanjiro ?", listOf("Respiration de l'Eau", "Respiration de la Flamme", "Respiration de la Foudre", "Respiration du Vent"), "Respiration de l'Eau"),

        // My Hero Academia
        Question("Quel est le pouvoir d'Izuku Midoriya dans My Hero Academia ?", listOf("One For All", "Explosion", "Création", "Effacement"), "One For All"),
        Question("Qui est le mentor d'Izuku Midoriya ?", listOf("All Might", "Endeavor", "Eraserhead", "Gran Torino"), "All Might"),
        Question("Quel est le nom du créateur de My Hero Academia ?", listOf("Kohei Horikoshi", "Eiichiro Oda", "Yoshihiro Togashi", "Tite Kubo"), "Kohei Horikoshi"),

        // Jeux vidéo
        Question("Dans quel jeu retrouve-t-on l'île d'Hyrule ?", listOf("The Legend of Zelda", "Final Fantasy", "Genshin Impact", "Elden Ring"), "The Legend of Zelda"),
        Question("Qui est le personnage principal de The Witcher ?", listOf("Geralt de Riv", "Link", "Cloud Strife", "Ezio Auditore"), "Geralt de Riv"),
        Question("Quel est le nom du héros principal de Super Mario ?", listOf("Mario", "Luigi", "Peach", "Bowser"), "Mario"),
        Question("Dans quel jeu retrouve-t-on le personnage de Link ?", listOf("The Legend of Zelda", "Super Mario", "Pokémon", "Final Fantasy"), "The Legend of Zelda"),
        Question("Quel est le nom du créateur de Minecraft ?", listOf("Markus Persson", "Shigeru Miyamoto", "Hideo Kojima", "Hironobu Sakaguchi"), "Markus Persson"),

        // Dessins animés
        Question("Quel est le nom du personnage principal de SpongeBob SquarePants ?", listOf("SpongeBob", "Patrick", "Sandy", "Mr. Krabs"), "SpongeBob"),
        Question("Dans quel dessin animé retrouve-t-on les personnages de Bugs Bunny et Daffy Duck ?", listOf("Looney Tunes", "Tom and Jerry", "Scooby-Doo", "The Flintstones"), "Looney Tunes"),
        Question("Quel est le nom du personnage principal de The Simpsons ?", listOf("Homer Simpson", "Bart Simpson", "Marge Simpson", "Lisa Simpson"), "Homer Simpson"),
        Question("Dans quel dessin animé retrouve-t-on les personnages de Mickey Mouse et Donald Duck ?", listOf("Mickey Mouse", "Tom and Jerry", "Scooby-Doo", "The Flintstones"), "Mickey Mouse"),
        Question("Quel est le nom du personnage principal de Adventure Time ?", listOf("Finn", "Jake", "Princess Bubblegum", "Ice King"), "Finn"),

        // Autres animes et mangas
        Question("Quel est le nom du personnage principal de Dragon Ball ?", listOf("Goku", "Vegeta", "Gohan", "Piccolo"), "Goku"),
        Question("Qui est le créateur de One Piece ?", listOf("Eiichiro Oda", "Masashi Kishimoto", "Tite Kubo", "Yoshihiro Togashi"), "Eiichiro Oda"),
        Question("Quel est le nom du personnage principal de Bleach ?", listOf("Ichigo Kurosaki", "Rukia Kuchiki", "Orihime Inoue", "Uryu Ishida"), "Ichigo Kurosaki"),
        Question("Quel est le nom du personnage principal de Fullmetal Alchemist ?", listOf("Edward Elric", "Alphonse Elric", "Winry Rockbell", "Roy Mustang"), "Edward Elric"),
        Question("Quel est le nom du personnage principal de Death Note ?", listOf("Light Yagami", "L", "Misa Amane", "Near"), "Light Yagami"),
        Question("Quel est le nom du personnage principal de Fairy Tail ?", listOf("Natsu Dragneel", "Lucy Heartfilia", "Erza Scarlet", "Gray Fullbuster"), "Natsu Dragneel"),
        Question("Quel est le nom du personnage principal de Sword Art Online ?", listOf("Kirito", "Asuna", "Leafa", "Klein"), "Kirito"),
        Question("Quel est le nom du personnage principal de Attack on Titan ?", listOf("Eren Yeager", "Mikasa Ackerman", "Armin Arlert", "Levi Ackerman"), "Eren Yeager"),
        Question("Quel est le nom du personnage principal de My Hero Academia ?", listOf("Izuku Midoriya", "Katsuki Bakugo", "Shoto Todoroki", "All Might"), "Izuku Midoriya"),
        Question("Quel est le nom du personnage principal de Demon Slayer ?", listOf("Tanjiro Kamado", "Nezuko Kamado", "Zenitsu Agatsuma", "Inosuke Hashibira"), "Tanjiro Kamado"),
        Question("Quel est le nom du personnage principal de Hunter x Hunter ?", listOf("Gon Freecss", "Killua Zoldyck", "Kurapika", "Leorio Paradinight"), "Gon Freecss"),
        Question("Quel est le nom du personnage principal de Naruto Shippuden ?", listOf("Naruto Uzumaki", "Sasuke Uchiha", "Sakura Haruno", "Kakashi Hatake"), "Naruto Uzumaki"),
        Question("Quel est le nom du personnage principal de One Punch Man ?", listOf("Saitama", "Genos", "Mumen Rider", "Speed-o'-Sound Sonic"), "Saitama"),
        Question("Quel est le nom du personnage principal de Black Clover ?", listOf("Asta", "Yuno", "Noelle Silva", "Gauche Adlai"), "Asta"),
        Question("Quel est le nom du personnage principal de JoJo's Bizarre Adventure ?", listOf("Jonathan Joestar", "Joseph Joestar", "Jotaro Kujo", "Josuke Higashikata"), "Jonathan Joestar"),
        Question("Quel est le nom du personnage principal de Re:Zero ?", listOf("Subaru Natsuki", "Emilia", "Rem", "Ram"), "Subaru Natsuki")
    )

    companion object {
        const val MAX_SCORE = 10
        const val MAX_INCORRECT = 3
        const val POINTS_PER_CORRECT = 1
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        initializeViews()
        setupGameStateObserver()
        setupClickListeners()

        gameState.value = GameState()
        loadQuestionAndOptions()
    }

    private fun initializeViews() {
        questionTextView = findViewById(R.id.questionTextView)
        answerButton1 = findViewById(R.id.answerButton1)
        answerButton2 = findViewById(R.id.answerButton2)
        answerButton3 = findViewById(R.id.answerButton3)
        answerButton4 = findViewById(R.id.answerButton4)
        scoreTextView = findViewById(R.id.scoreTextView)
        buttonReturn = findViewById(R.id.buttonReturn)
    }

    private fun setupGameStateObserver() {
        gameState.observe(this) { state ->
            scoreTextView.text = getString(R.string.score_format, state.score)
        }
    }

    private fun setupClickListeners() {
        answerButton1.setOnClickListener { checkAnswer(answerButton1.text.toString()) }
        answerButton2.setOnClickListener { checkAnswer(answerButton2.text.toString()) }
        answerButton3.setOnClickListener { checkAnswer(answerButton3.text.toString()) }
        answerButton4.setOnClickListener { checkAnswer(answerButton4.text.toString()) }

        buttonReturn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadQuestionAndOptions() {
        gameState.value?.let { state ->
            // Select a random unused question
            val availableIndices = questions.indices.toSet() - usedQuestions
            if (availableIndices.isEmpty()) {
                usedQuestions.clear() // Reset if all questions have been used
            }

            state.currentQuestionIndex = availableIndices.random()
            usedQuestions.add(state.currentQuestionIndex)

            val currentQuestion = questions[state.currentQuestionIndex]

            // Update UI
            questionTextView.text = currentQuestion.text
            setupOptions(currentQuestion)
        }
    }

    private fun setupOptions(currentQuestion: Question) {
        val shuffledAnswers = currentQuestion.answers.shuffled()
        answerButton1.text = shuffledAnswers[0]
        answerButton2.text = shuffledAnswers[1]
        answerButton3.text = shuffledAnswers[2]
        answerButton4.text = shuffledAnswers[3]
    }

    private fun checkAnswer(selectedAnswer: String) {
        gameState.value?.let { state ->
            val currentQuestion = questions[state.currentQuestionIndex]

            if (selectedAnswer == currentQuestion.correctAnswer) {
                handleCorrectAnswer(state)
            } else {
                handleIncorrectAnswer(state, currentQuestion.correctAnswer)
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