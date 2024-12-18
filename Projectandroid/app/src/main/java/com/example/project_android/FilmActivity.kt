package com.example.project_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_android.R.id.buttonReturn
import kotlin.random.Random

// Classe principale pour l'activité Quiz sur les films
class FilmActivity : AppCompatActivity() {

    private lateinit var quizGame: QuizGame

    // Liste des questions spécifiques aux films
    private val filmQuestions = listOf(
        // Films d'action
        Question("Quel est le nom du héros principal dans la série de films 'Die Hard' ?", listOf("John McClane", "Bruce Willis", "Hans Gruber", "Al Powell"), "John McClane"),
        Question("Qui joue le rôle de James Bond dans 'Casino Royale' ?", listOf("Daniel Craig", "Pierce Brosnan", "Sean Connery", "Roger Moore"), "Daniel Craig"),
        Question("Quel est le nom du réalisateur de 'Mad Max: Fury Road' ?", listOf("George Miller", "James Cameron", "Christopher Nolan", "Quentin Tarantino"), "George Miller"),
        Question("Dans quel film Keanu Reeves joue-t-il le rôle de Neo ?", listOf("The Matrix", "Speed", "John Wick", "Point Break"), "The Matrix"),
        Question("Quel est le nom du personnage principal dans 'John Wick' ?", listOf("John Wick", "Keanu Reeves", "Viggo Tarasov", "Marcus"), "John Wick"),

        // Films de science-fiction
        Question("Qui joue le rôle de Luke Skywalker dans 'Star Wars' ?", listOf("Mark Hamill", "Harrison Ford", "Carrie Fisher", "Alec Guinness"), "Mark Hamill"),
        Question("Quel est le nom du réalisateur de 'Inception' ?", listOf("Christopher Nolan", "Steven Spielberg", "James Cameron", "Ridley Scott"), "Christopher Nolan"),
        Question("Dans quel film Sigourney Weaver joue-t-elle le rôle de Ripley ?", listOf("Alien", "Avatar", "Ghostbusters", "Gorillas in the Mist"), "Alien"),
        Question("Quel est le nom du réalisateur de 'Blade Runner' ?", listOf("Ridley Scott", "Steven Spielberg", "James Cameron", "George Lucas"), "Ridley Scott"),
        Question("Dans quel film Arnold Schwarzenegger joue-t-il le rôle du Terminator ?", listOf("The Terminator", "Predator", "Total Recall", "Commando"), "The Terminator"),

        // Films de super-héros
        Question("Qui joue le rôle de Tony Stark/Iron Man dans les films Marvel ?", listOf("Robert Downey Jr.", "Chris Evans", "Chris Hemsworth", "Mark Ruffalo"), "Robert Downey Jr."),
        Question("Quel est le nom du réalisateur de 'The Dark Knight' ?", listOf("Christopher Nolan", "Zack Snyder", "Joss Whedon", "Jon Favreau"), "Christopher Nolan"),
        Question("Dans quel film Chris Evans joue-t-il le rôle de Captain America ?", listOf("Captain America: The First Avenger", "The Avengers", "Iron Man", "Thor"), "Captain America: The First Avenger"),
        Question("Quel est le nom du réalisateur de 'Guardians of the Galaxy' ?", listOf("James Gunn", "Joss Whedon", "Anthony Russo", "Joe Russo"), "James Gunn"),
        Question("Dans quel film Gal Gadot joue-t-elle le rôle de Wonder Woman ?", listOf("Wonder Woman", "Batman v Superman: Dawn of Justice", "Justice League", "Man of Steel"), "Wonder Woman"),

        // Films d'animation
        Question("Quel est le nom du réalisateur de 'Toy Story' ?", listOf("John Lasseter", "Pete Docter", "Andrew Stanton", "Lee Unkrich"), "John Lasseter"),
        Question("Dans quel film trouve-t-on le personnage de Nemo ?", listOf("Finding Nemo", "The Little Mermaid", "Shark Tale", "Moana"), "Finding Nemo"),
        Question("Quel est le nom du réalisateur de 'Spirited Away' ?", listOf("Hayao Miyazaki", "Isao Takahata", "Makoto Shinkai", "Mamoru Hosoda"), "Hayao Miyazaki"),
        Question("Dans quel film trouve-t-on le personnage de Woody ?", listOf("Toy Story", "The Incredibles", "Cars", "Ratatouille"), "Toy Story"),
        Question("Quel est le nom du réalisateur de 'Inside Out' ?", listOf("Pete Docter", "John Lasseter", "Andrew Stanton", "Lee Unkrich"), "Pete Docter"),

        // Films de comédie
        Question("Qui joue le rôle de Forrest Gump dans le film du même nom ?", listOf("Tom Hanks", "Robin Wright", "Sally Field", "Gary Sinise"), "Tom Hanks"),
        Question("Quel est le nom du réalisateur de 'Superbad' ?", listOf("Greg Mottola", "Judd Apatow", "Seth Rogen", "Evan Goldberg"), "Greg Mottola"),
        Question("Dans quel film Jim Carrey joue-t-il le rôle de Lloyd Christmas ?", listOf("Dumb and Dumber", "Ace Ventura: Pet Detective", "The Mask", "Liar Liar"), "Dumb and Dumber"),
        Question("Quel est le nom du réalisateur de 'The Hangover' ?", listOf("Todd Phillips", "Zach Galifianakis", "Bradley Cooper", "Ed Helms"), "Todd Phillips"),
        Question("Dans quel film Will Ferrell joue-t-il le rôle de Buddy the Elf ?", listOf("Elf", "Anchorman: The Legend of Ron Burgundy", "Talladega Nights: The Ballad of Ricky Bobby", "Step Brothers"), "Elf"),

        // Films de drame
        Question("Qui joue le rôle de Forrest Gump dans le film du même nom ?", listOf("Tom Hanks", "Robin Wright", "Sally Field", "Gary Sinise"), "Tom Hanks"),
        Question("Quel est le nom du réalisateur de 'The Shawshank Redemption' ?", listOf("Frank Darabont", "Stephen King", "Morgan Freeman", "Tim Robbins"), "Frank Darabont"),
        Question("Dans quel film Leonardo DiCaprio joue-t-il le rôle de Jack Dawson ?", listOf("Titanic", "The Revenant", "Inception", "The Wolf of Wall Street"), "Titanic"),
        Question("Quel est le nom du réalisateur de 'The Pianist' ?", listOf("Roman Polanski", "Adrien Brody", "Thomas Kretschmann", "Emilia Fox"), "Roman Polanski"),
        Question("Dans quel film Meryl Streep joue-t-elle le rôle de Miranda Priestly ?", listOf("The Devil Wears Prada", "Mamma Mia!", "The Iron Lady", "Julie & Julia"), "The Devil Wears Prada"),

        // Films de fantasy
        Question("Quel est le nom du réalisateur de 'The Lord of the Rings' trilogy ?", listOf("Peter Jackson", "J.R.R. Tolkien", "Ian McKellen", "Viggo Mortensen"), "Peter Jackson"),
        Question("Dans quel film Daniel Radcliffe joue-t-il le rôle de Harry Potter ?", listOf("Harry Potter and the Philosopher's Stone", "Harry Potter and the Chamber of Secrets", "Harry Potter and the Prisoner of Azkaban", "Harry Potter and the Goblet of Fire"), "Harry Potter and the Philosopher's Stone"),
        Question("Quel est le nom du réalisateur de 'Pan's Labyrinth' ?", listOf("Guillermo del Toro", "Alfonso Cuarón", "Alejandro González Iñárritu", "Pedro Almodóvar"), "Guillermo del Toro"),
        Question("Dans quel film Johnny Depp joue-t-il le rôle de Jack Sparrow ?", listOf("Pirates of the Caribbean: The Curse of the Black Pearl", "Edward Scissorhands", "Alice in Wonderland", "Charlie and the Chocolate Factory"), "Pirates of the Caribbean: The Curse of the Black Pearl"),
        Question("Quel est le nom du réalisateur de 'The Hobbit' trilogy ?", listOf("Peter Jackson", "J.R.R. Tolkien", "Ian McKellen", "Martin Freeman"), "Peter Jackson"),

        // Films d'horreur
        Question("Quel est le nom du réalisateur de 'The Exorcist' ?", listOf("William Friedkin", "Linda Blair", "Ellen Burstyn", "Max von Sydow"), "William Friedkin"),
        Question("Dans quel film Jamie Lee Curtis joue-t-elle le rôle de Laurie Strode ?", listOf("Halloween", "Scream", "The Fog", "Prom Night"), "Halloween"),
        Question("Quel est le nom du réalisateur de 'The Shining' ?", listOf("Stanley Kubrick", "Jack Nicholson", "Shelley Duvall", "Danny Lloyd"), "Stanley Kubrick"),
        Question("Dans quel film Sigourney Weaver joue-t-elle le rôle de Ripley ?", listOf("Alien", "Avatar", "Ghostbusters", "Gorillas in the Mist"), "Alien"),
        Question("Quel est le nom du réalisateur de 'Get Out' ?", listOf("Jordan Peele", "Daniel Kaluuya", "Allison Williams", "Bradley Whitford"), "Jordan Peele")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)

        // Mélanger les questions
        val shuffledQuestions = filmQuestions.shuffled()

        // Initialiser le jeu avec les questions mélangées
        quizGame = QuizGame(shuffledQuestions)

        // Initialisation des vues
        val questionTextView: TextView = findViewById(R.id.questionTextView)
        val answerButton1: Button = findViewById(R.id.answerButton1)
        val answerButton2: Button = findViewById(R.id.answerButton2)
        val answerButton3: Button = findViewById(R.id.answerButton3)
        val answerButton4: Button = findViewById(R.id.answerButton4)
        val scoreTextView: TextView = findViewById(R.id.scoreTextView)
        val buttonReturn : Button = findViewById(buttonReturn)

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
                val intent = Intent(this, HomeActivity::class.java) // Redirection vers une autre activité
                startActivity(intent)
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

            // Passer à la question suivante
            quizGame.nextQuestion()
            checkGameStatus()
            loadQuestion()
        }

        // Gestion de l'événement au clic sur chaque bouton de réponse
        answerButton1.setOnClickListener { onAnswerSelected(answerButton1.text.toString()) }
        answerButton2.setOnClickListener { onAnswerSelected(answerButton2.text.toString()) }
        answerButton3.setOnClickListener { onAnswerSelected(answerButton3.text.toString()) }
        answerButton4.setOnClickListener { onAnswerSelected(answerButton4.text.toString()) }

        buttonReturn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}

// Classe pour représenter une question
data class Questione(val text: String, val answers: List<String>, val correctAnswer: String)

// Classe pour gérer la logique du quiz
class QuizGamee(private val questions: List<Question>) {
    private var currentQuestionIndex = 0
    var score = 0
    var wrongAnswers = 0

    fun getCurrentQuestion(): Question {
        return questions[currentQuestionIndex]
    }

    fun checkAnswer(answer: String): Boolean {
        return getCurrentQuestion().correctAnswer == answer
    }

    fun nextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
        }
    }

    fun isGameOver(): Boolean {
        return currentQuestionIndex >= questions.size - 1
    }

    fun getGameStatus(): String {
        return "Quiz terminé ! Votre score est de $score sur ${questions.size}"
    }
}
