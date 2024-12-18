package com.example.project_android

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_android.R.id.buttonReturn
import kotlin.random.Random

// Classe représentant une question et ses réponses possibles
data class Question(val text: String, val answers: List<String>, val correctAnswer: String)

// Classe représentant la logique du jeu
class QuizGame(private val questions: List<Question>) {
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
class QuizActivity : AppCompatActivity() {
    private lateinit var quizGame: QuizGame

    // Liste des questions avec leurs réponses possibles
    // Liste des questions avec leurs réponses possibles
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
        Question("Quel est le nom du personnage principal de Re:Zero ?", listOf("Subaru Natsuki", "Emilia", "Rem", "Ram"), "Subaru Natsuki"),
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
        Question("Quel est le nom du personnage principal de Re:Zero ?", listOf("Subaru Natsuki", "Emilia", "Rem", "Ram"), "Subaru Natsuki"),
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

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