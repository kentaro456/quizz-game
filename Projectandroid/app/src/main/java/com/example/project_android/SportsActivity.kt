package com.example.project_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


data class Q(val text: String, val answers: List<String>, val correctAnswer: String)

// Classe représentant la logique du jeu
class QuizGamek(private val questions: List<Question>) {
    private var currentQuestionIndex = 0
    var score = 0
    var wrongAnswers = 0

    // Fonction pour obtenir la question actuelle
    fun getCurrentQuestion(): Question {
        return questions[currentQuestionIndex]
    }

    // Fonction pour vérifier si la réponse est correcte
    fun checkAnswer(userAnswer: String): Boolean {
        val currentQuestion = getCurrentQuestion()
        return userAnswer.trim().equals(currentQuestion.correctAnswer, ignoreCase = true)
    }

    // Fonction pour passer à la question suivante
    fun nextQuestion() {
        currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
    }

    // Fonction pour réinitialiser le jeu
    fun restartGame() {
        score = 0
        wrongAnswers = 0
        currentQuestionIndex = 0
    }

    // Fonction pour vérifier si le jeu est terminé (victoire ou défaite)
    fun isGameOver(): Boolean {
        return score >= 10 || wrongAnswers >= 3
    }

    // Fonction pour obtenir l'état du jeu (victoire ou défaite)
    fun getGameStatus(): String {
        return when {
            score >= 10 -> "Félicitations, vous avez gagné !"
            wrongAnswers >= 3 -> "Désolé, vous avez perdu !"
            else -> "Continuez à jouer !"
        }
    }
}

// Classe principale de l'activité qui gère l'interface utilisateur
class SportsActivity : AppCompatActivity() {
    private lateinit var quizGame: QuizGame

    // Liste des questions avec leurs réponses possibles
    // Liste des questions avec leurs réponses possibles
    private val questions = listOf(
        Question("Quel sport est connu sous le nom de 'football' en Amérique ?", listOf("Football américain", "Soccer", "Rugby", "Baseball"), "Football américain"),
        Question("Qui a remporté la Coupe du Monde de la FIFA 2018 ?", listOf("France", "Croatie", "Allemagne", "Brésil"), "France"),
        Question("Quel sport utilise une raquette et une petite balle jaune ?", listOf("Tennis", "Badminton", "Ping-pong", "Squash"), "Tennis"),
        Question("Quel sport est pratiqué sur une piste ovale avec des voitures de course ?", listOf("Formule 1", "NASCAR", "Rallye", "MotoGP"), "Formule 1"),
        Question("Quel sport est connu pour ses combats en cage octogonale ?", listOf("MMA", "Boxe", "Karaté", "Judo"), "MMA"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de H ?", listOf("Rugby", "Football américain", "Football australien", "Gaelic football"), "Rugby"),
        Question("Quel sport est pratiqué sur une piste de glace avec des patins et un palet ?", listOf("Hockey sur glace", "Patinage artistique", "Curling", "Bobsleigh"), "Hockey sur glace"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des buts ?", listOf("Football", "Basketball", "Volleyball", "Handball"), "Football"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de Y ?", listOf("Football américain", "Rugby", "Football australien", "Gaelic football"), "Football américain"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des paniers ?", listOf("Basketball", "Netball", "Handball", "Volleyball"), "Basketball"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de H ?", listOf("Rugby", "Football américain", "Football australien", "Gaelic football"), "Rugby"),
        Question("Quel sport est pratiqué sur une piste de glace avec des patins et un palet ?", listOf("Hockey sur glace", "Patinage artistique", "Curling", "Bobsleigh"), "Hockey sur glace"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des buts ?", listOf("Football", "Basketball", "Volleyball", "Handball"), "Football"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de Y ?", listOf("Football américain", "Rugby", "Football australien", "Gaelic football"), "Football américain"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des paniers ?", listOf("Basketball", "Netball", "Handball", "Volleyball"), "Basketball"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de H ?", listOf("Rugby", "Football américain", "Football australien", "Gaelic football"), "Rugby"),
        Question("Quel sport est pratiqué sur une piste de glace avec des patins et un palet ?", listOf("Hockey sur glace", "Patinage artistique", "Curling", "Bobsleigh"), "Hockey sur glace"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des buts ?", listOf("Football", "Basketball", "Volleyball", "Handball"), "Football"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de Y ?", listOf("Football américain", "Rugby", "Football australien", "Gaelic football"), "Football américain"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des paniers ?", listOf("Basketball", "Netball", "Handball", "Volleyball"), "Basketball"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de H ?", listOf("Rugby", "Football américain", "Football australien", "Gaelic football"), "Rugby"),
        Question("Quel sport est pratiqué sur une piste de glace avec des patins et un palet ?", listOf("Hockey sur glace", "Patinage artistique", "Curling", "Bobsleigh"), "Hockey sur glace"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des buts ?", listOf("Football", "Basketball", "Volleyball", "Handball"), "Football"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de Y ?", listOf("Football américain", "Rugby", "Football australien", "Gaelic football"), "Football américain"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des paniers ?", listOf("Basketball", "Netball", "Handball", "Volleyball"), "Basketball"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de H ?", listOf("Rugby", "Football américain", "Football australien", "Gaelic football"), "Rugby"),
        Question("Quel sport est pratiqué sur une piste de glace avec des patins et un palet ?", listOf("Hockey sur glace", "Patinage artistique", "Curling", "Bobsleigh"), "Hockey sur glace"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des buts ?", listOf("Football", "Basketball", "Volleyball", "Handball"), "Football"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de Y ?", listOf("Football américain", "Rugby", "Football australien", "Gaelic football"), "Football américain"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des paniers ?", listOf("Basketball", "Netball", "Handball", "Volleyball"), "Basketball"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de H ?", listOf("Rugby", "Football américain", "Football australien", "Gaelic football"), "Rugby"),
        Question("Quel sport est pratiqué sur une piste de glace avec des patins et un palet ?", listOf("Hockey sur glace", "Patinage artistique", "Curling", "Bobsleigh"), "Hockey sur glace"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des buts ?", listOf("Football", "Basketball", "Volleyball", "Handball"), "Football"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de Y ?", listOf("Football américain", "Rugby", "Football australien", "Gaelic football"), "Football américain"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des paniers ?", listOf("Basketball", "Netball", "Handball", "Volleyball"), "Basketball"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de H ?", listOf("Rugby", "Football américain", "Football australien", "Gaelic football"), "Rugby"),
        Question("Quel sport est pratiqué sur une piste de glace avec des patins et un palet ?", listOf("Hockey sur glace", "Patinage artistique", "Curling", "Bobsleigh"), "Hockey sur glace"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des buts ?", listOf("Football", "Basketball", "Volleyball", "Handball"), "Football"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de Y ?", listOf("Football américain", "Rugby", "Football australien", "Gaelic football"), "Football américain"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des paniers ?", listOf("Basketball", "Netball", "Handball", "Volleyball"), "Basketball"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de H ?", listOf("Rugby", "Football américain", "Football australien", "Gaelic football"), "Rugby"),
        Question("Quel sport est pratiqué sur une piste de glace avec des patins et un palet ?", listOf("Hockey sur glace", "Patinage artistique", "Curling", "Bobsleigh"), "Hockey sur glace"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des buts ?", listOf("Football", "Basketball", "Volleyball", "Handball"), "Football"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de Y ?", listOf("Football américain", "Rugby", "Football australien", "Gaelic football"), "Football américain"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des paniers ?", listOf("Basketball", "Netball", "Handball", "Volleyball"), "Basketball"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de H ?", listOf("Rugby", "Football américain", "Football australien", "Gaelic football"), "Rugby"),
        Question("Quel sport est pratiqué sur une piste de glace avec des patins et un palet ?", listOf("Hockey sur glace", "Patinage artistique", "Curling", "Bobsleigh"), "Hockey sur glace"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des buts ?", listOf("Football", "Basketball", "Volleyball", "Handball"), "Football"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de Y ?", listOf("Football américain", "Rugby", "Football australien", "Gaelic football"), "Football américain"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des paniers ?", listOf("Basketball", "Netball", "Handball", "Volleyball"), "Basketball"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon ovale et des buts en forme de H ?", listOf("Rugby", "Football américain", "Football australien", "Gaelic football"), "Rugby"),
        Question("Quel sport est pratiqué sur une piste de glace avec des patins et un palet ?", listOf("Hockey sur glace", "Patinage artistique", "Curling", "Bobsleigh"), "Hockey sur glace"),
        Question("Quel sport est pratiqué sur un terrain avec un ballon rond et des buts ?", listOf("Football", "Basketball", "Volleyball", "Handball"), "Football"),

    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports)

        // Mélanger les questions
        val shuffledQuestions = questions.shuffled()

        // Initialiser le jeu avec les questions mélangées
        quizGame = QuizGame(shuffledQuestions)

        // Initialisation des vues
        val questionTextView: TextView = findViewById(R.id.questionTextView)
        val answerButton1: Button = findViewById(R.id.answerButton1)
        val answerButton2: Button = findViewById(R.id.answerButton2)
        val answerButton3: Button = findViewById(R.id.answerButton3)
        val answerButton4: Button = findViewById(R.id.answerButton4)
        val scoreTextView: TextView = findViewById(R.id.scoreTextView)
        val buttonReturn: Button = findViewById(R.id.buttonReturn)

        // Fonction pour charger la question actuelle et les réponses
        fun loadQuestion() {
            val currentQuestion = quizGame.getCurrentQuestion()
            questionTextView.text = currentQuestion.text

            // Mélanger les réponses
            val shuffledAnswers = currentQuestion.answers.shuffled()
            answerButton1.text = shuffledAnswers[0]
            answerButton2.text = shuffledAnswers[1]
            answerButton3.text = shuffledAnswers[2]
            answerButton4.text = shuffledAnswers[3]
        }

        // Fonction pour mettre à jour le score affiché
        fun updateScore() {
            scoreTextView.text = "Score: ${quizGame.score}"
        }

        // Fonction pour gérer la fin du jeu
        fun checkGameStatus() {
            if (quizGame.isGameOver()) {
                Toast.makeText(this, quizGame.getGameStatus(), Toast.LENGTH_LONG).show()
                // Vous pouvez rediriger vers une autre activité ou terminer le quiz
                quizGame.restartGame()
                loadQuestion()
                updateScore()
                // Rediriger vers une autre activité (par exemple, le menu principal)
                val intent = Intent(this, HomeActivity::class.java) // Remplacez HomeActivity par votre activité cible
                startActivity(intent)

                // Vous pouvez également terminer l'activité actuelle si vous ne souhaitez pas revenir à celle-ci
                finish()
            }
        }

        // Initialisation de la première question et du score
        loadQuestion()
        updateScore()

        // Fonction générique pour gérer les réponses
        fun onAnswerSelected(answer: String) {
            if (quizGame.checkAnswer(answer)) {
                quizGame.score++
            } else {
                quizGame.wrongAnswers++
                Toast.makeText(this, "Mauvaise réponse ! Erreurs: ${quizGame.wrongAnswers}", Toast.LENGTH_SHORT).show()
            }

            updateScore()
            checkGameStatus()

            // Passer à la question suivante avec un délai pour mieux gérer les réponses rapides
            Handler().postDelayed({
                quizGame.nextQuestion()
                loadQuestion()
            }, 1000) // 1 seconde de délai
        }

        // Gestion de l'événement au clic sur chaque bouton de réponse
        answerButton1.setOnClickListener { onAnswerSelected(answerButton1.text.toString()) }
        answerButton2.setOnClickListener { onAnswerSelected(answerButton2.text.toString()) }
        answerButton3.setOnClickListener { onAnswerSelected(answerButton3.text.toString()) }
        answerButton4.setOnClickListener { onAnswerSelected(answerButton4.text.toString()) }

        // Gestion de l'événement au clic sur le bouton "Retour"
        buttonReturn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}