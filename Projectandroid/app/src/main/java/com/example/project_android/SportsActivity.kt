package com.example.project_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData

data class Questionz(val text: String, val answers: List<String>, val correctAnswer: String)

class SportsActivity : AppCompatActivity() {
    private lateinit var questionTextView: TextView
    private lateinit var answerButtons: List<Button>
    private lateinit var scoreTextView: TextView
    private lateinit var buttonReturn: Button

    private val gameState = MutableLiveData<GameState>()
    private var usedQuestions = mutableSetOf<Int>()

    private data class GameState(var score: Int = 0, var incorrectAnswers: Int = 0, var currentQuestionIndex: Int = -1)

    private val questions = listOf(
        Questionz("Quel pays a remporté la Coupe du Monde de la FIFA 2018 ?", listOf("France", "Croatie", "Allemagne", "Brésil"), "France"),
        Questionz("Qui est considéré comme le meilleur joueur de football de tous les temps ?", listOf("Pelé", "Diego Maradona", "Lionel Messi", "Cristiano Ronaldo"), "Pelé"),
        Questionz("Quel club de football a remporté le plus de titres de la Ligue des Champions de l'UEFA ?", listOf("Real Madrid", "Barcelona", "Manchester United", "Bayern Munich"), "Real Madrid"),
        Questionz("Qui a remporté le Ballon d'Or en 2021 ?", listOf("Lionel Messi", "Robert Lewandowski", "Cristiano Ronaldo", "Kylian Mbappé"), "Lionel Messi"),
        Questionz("Quel joueur de basketball est connu sous le surnom de 'The Black Mamba' ?", listOf("Kobe Bryant", "Michael Jordan", "LeBron James", "Shaquille O'Neal"), "Kobe Bryant"),
        Questionz("Quelle équipe de la NBA a remporté le plus de titres de championnat ?", listOf("Boston Celtics", "Los Angeles Lakers", "Chicago Bulls", "Golden State Warriors"), "Boston Celtics"),
        Questionz("Qui est considéré comme le meilleur joueur de basketball de tous les temps ?", listOf("Michael Jordan", "LeBron James", "Kareem Abdul-Jabbar", "Magic Johnson"), "Michael Jordan"),
        Questionz("Quel combattant de MMA est connu sous le surnom de 'The Notorious' ?", listOf("Conor McGregor", "Jon Jones", "Khabib Nurmagomedov", "Anderson Silva"), "Conor McGregor"),
        Questionz("Quel combattant de MMA est connu pour son style de combat de sambo ?", listOf("Khabib Nurmagomedov", "Fedor Emelianenko", "Daniel Cormier", "Georges St-Pierre"), "Khabib Nurmagomedov"),
        Questionz("Quel boxeur est connu sous le surnom de 'The Greatest' ?", listOf("Muhammad Ali", "Mike Tyson", "Floyd Mayweather Jr.", "Manny Pacquiao"), "Muhammad Ali"),
        Questionz("Quel boxeur est connu sous le surnom de 'Iron Mike' ?", listOf("Mike Tyson", "Muhammad Ali", "Floyd Mayweather Jr.", "Manny Pacquiao"), "Mike Tyson"),
        Questionz("Quel club de football a remporté la Premier League anglaise en 2021 ?", listOf("Manchester City", "Liverpool", "Chelsea", "Manchester United"), "Manchester City"),
        Questionz("Quel joueur de basketball a remporté le plus de titres de MVP de la NBA ?", listOf("Kareem Abdul-Jabbar", "Michael Jordan", "LeBron James", "Magic Johnson"), "Kareem Abdul-Jabbar"),
        Questionz("Quel combattant de MMA est connu sous le surnom de 'The Spider' ?", listOf("Anderson Silva", "Jon Jones", "Georges St-Pierre", "Daniel Cormier"), "Anderson Silva"),
        Questionz("Quel boxeur est connu sous le surnom de 'Money' ?", listOf("Floyd Mayweather Jr.", "Manny Pacquiao", "Oscar De La Hoya", "Canelo Alvarez"), "Floyd Mayweather Jr."),
        Questionz("Quel joueur de football a remporté le plus de Ballons d'Or ?", listOf("Lionel Messi", "Cristiano Ronaldo", "Johan Cruyff", "Michel Platini"), "Lionel Messi"),
        Questionz("Quel joueur de basketball a marqué le plus de points en une seule saison de NBA ?", listOf("Wilt Chamberlain", "Michael Jordan", "Kobe Bryant", "LeBron James"), "Wilt Chamberlain"),
        Questionz("Quel combattant de MMA est connu sous le surnom de 'The Eagle' ?", listOf("Khabib Nurmagomedov", "Conor McGregor", "Jon Jones", "Daniel Cormier"), "Khabib Nurmagomedov"),
        Questionz("Quel boxeur est connu sous le surnom de 'Pacman' ?", listOf("Manny Pacquiao", "Floyd Mayweather Jr.", "Oscar De La Hoya", "Canelo Alvarez"), "Manny Pacquiao"),
        Questionz("Quel club de football a remporté la Liga espagnole en 2021 ?", listOf("Atlético Madrid", "Real Madrid", "Barcelona", "Sevilla"), "Atlético Madrid")
    )


    companion object {
        const val MAX_SCORE = 10
        const val MAX_INCORRECT = 3
        const val POINTS_PER_CORRECT = 1
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports)

        initializeViews()
        setupGameStateObserver()
        setupClickListeners()

        gameState.value = GameState()
        loadQuestionAndOptions()
    }

    private fun initializeViews() {
        questionTextView = findViewById(R.id.questionTextView)
        answerButtons = listOf(
            findViewById(R.id.answerButton1),
            findViewById(R.id.answerButton2),
            findViewById(R.id.answerButton3),
            findViewById(R.id.answerButton4)
        )
        scoreTextView = findViewById(R.id.scoreTextView)
        buttonReturn = findViewById(R.id.buttonReturn)
    }

    private fun setupGameStateObserver() {
        gameState.observe(this) { state ->
            scoreTextView.text = getString(R.string.score_format, state.score)
        }
    }

    private fun setupClickListeners() {
        answerButtons.forEach { button ->
            button.setOnClickListener { checkAnswer(button.text.toString()) }
        }

        buttonReturn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun loadQuestionAndOptions() {
        gameState.value?.let { state ->
            val availableIndices = questions.indices.toSet() - usedQuestions
            if (availableIndices.isEmpty()) usedQuestions.clear() // Reset if all questions used

            state.currentQuestionIndex = availableIndices.random()
            usedQuestions.add(state.currentQuestionIndex)

            val currentQuestion = questions[state.currentQuestionIndex]
            questionTextView.text = currentQuestion.text
            setupOptions(currentQuestion)
        }
    }

    private fun setupOptions(currentQuestion: Questionz) {
        val shuffledAnswers = currentQuestion.answers.shuffled()
        answerButtons.forEachIndexed { index, button ->
            button.text = shuffledAnswers[index]
        }
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
        showAnswerDialog(isCorrect = true)

        if (state.score >= MAX_SCORE) {
            endGame(getString(R.string.win_message))
        } else {
            gameState.value = state
            loadQuestionAndOptions()
        }
    }

    private fun handleIncorrectAnswer(state: GameState, correctAnswer: String) {
        state.incorrectAnswers++
        showAnswerDialog(isCorrect = false, correctAnswer)

        if (state.incorrectAnswers >= MAX_INCORRECT) {
            endGame(getString(R.string.game_over_message))
        } else {
            gameState.value = state
            loadQuestionAndOptions()
        }
    }

    private fun showAnswerDialog(isCorrect: Boolean, correctAnswer: String = "") {
        val message = if (isCorrect) {
            getString(R.string.correct_message)
        } else {
            getString(R.string.incorrect_message, correctAnswer)
        }
        AlertDialog.Builder(this)
            .setTitle(if (isCorrect) getString(R.string.correct) else getString(R.string.incorrect))
            .setMessage(message)
            .setPositiveButton(getString(R.string.continue_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun endGame(message: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.game_ended))
            .setMessage(message)
            .setPositiveButton(getString(R.string.return_home)) { _, _ -> goToHome() }
            .setCancelable(false)
            .show()
    }

    private fun goToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
