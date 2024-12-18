package  com.example.project_android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

// Classe représentant une question et ses réponses possibles
data class Questions(val text: String, val answers: List<String>, val correctAnswer: String)

// Classe représentant la logique du jeu
class QuizGames(private val questions: List<Question>) {
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
class SeriesActivity : AppCompatActivity() {
    private lateinit var quizGame: QuizGame

    // Liste des questions avec leurs réponses possibles
// Liste des questions avec leurs réponses possibles
    private val questions = listOf(
        // Breaking Bad
        Question("Qui est le personnage principal de Breaking Bad ?", listOf("Walter White", "Jesse Pinkman", "Skyler White", "Hank Schrader"), "Walter White"),
        Question("Quel est le pseudonyme de Walter White dans Breaking Bad ?", listOf("Heisenberg", "Walter Jr.", "Flynn", "Saul Goodman"), "Heisenberg"),
        Question("Quel est le métier de Walter White avant de devenir un fabricant de méthamphétamine ?", listOf("Professeur de chimie", "Avocat", "Policier", "Médecin"), "Professeur de chimie"),

        // Game of Thrones
        Question("Quel est le nom de la famille royale dans Game of Thrones ?", listOf("Lannister", "Stark", "Targaryen", "Baratheon"), "Lannister"),
        Question("Qui est le roi des Sept Couronnes au début de Game of Thrones ?", listOf("Robert Baratheon", "Joffrey Baratheon", "Tommen Baratheon", "Renly Baratheon"), "Robert Baratheon"),
        Question("Quel est le nom de la capitale des Sept Couronnes dans Game of Thrones ?", listOf("King's Landing", "Winterfell", "Casterly Rock", "Dragonstone"), "King's Landing"),

        // Stranger Things
        Question("Quel est le nom de la ville où se déroule Stranger Things ?", listOf("Hawkins", "Springfield", "Riverdale", "Sunnydale"), "Hawkins"),
        Question("Quel est le nom du monstre principal dans la première saison de Stranger Things ?", listOf("Demogorgon", "Mind Flayer", "Vecna", "The Upside Down"), "Demogorgon"),
        Question("Qui est le chef de la police dans Stranger Things ?", listOf("Jim Hopper", "Joyce Byers", "Mike Wheeler", "Dustin Henderson"), "Jim Hopper"),

        // The Walking Dead
        Question("Qui est le personnage principal de The Walking Dead ?", listOf("Rick Grimes", "Daryl Dixon", "Michonne", "Carl Grimes"), "Rick Grimes"),
        Question("Quel est le nom de l'arme préférée de Daryl Dixon dans The Walking Dead ?", listOf("Arbalète", "Fusil", "Épée", "Hache"), "Arbalète"),
        Question("Quel est le nom de la prison où le groupe se réfugie dans la saison 3 de The Walking Dead ?", listOf("Prison de West Georgia", "Terminus", "Alexandria", "Hilltop"), "Prison de West Georgia"),

        // Friends
        Question("Quel est le nom du café où les amis se retrouvent dans Friends ?", listOf("Central Perk", "Monica's Apartment", "Joey's Apartment", "Ross's Apartment"), "Central Perk"),
        Question("Quel est le métier de Ross Geller dans Friends ?", listOf("Paléontologue", "Acteur", "Chef", "Médecin"), "Paléontologue"),
        Question("Quel est le nom du singe de Ross dans Friends ?", listOf("Marcel", "Joey", "Chandler", "Phoebe"), "Marcel"),

        // The Office (US)
        Question("Quel est le nom du patron dans The Office (US) ?", listOf("Michael Scott", "Dwight Schrute", "Jim Halpert", "Pam Beesly"), "Michael Scott"),
        Question("Quel est le nom de l'entreprise où se déroule The Office (US) ?", listOf("Dunder Mifflin", "Vance Refrigeration", "Sabre", "Staples"), "Dunder Mifflin"),
        Question("Quel est le nom de l'assistante de Michael Scott dans The Office (US) ?", listOf("Pam Beesly", "Erin Hannon", "Kelly Kapoor", "Angela Martin"), "Pam Beesly"),

        // Sherlock
        Question("Qui joue le rôle de Sherlock Holmes dans la série Sherlock ?", listOf("Benedict Cumberbatch", "Martin Freeman", "Rupert Graves", "Mark Gatiss"), "Benedict Cumberbatch"),
        Question("Quel est le nom de l'adresse de Sherlock Holmes dans la série Sherlock ?", listOf("221B Baker Street", "10 Downing Street", "12 Grimmauld Place", "4 Privet Drive"), "221B Baker Street"),
        Question("Quel est le nom du frère de Sherlock Holmes dans la série Sherlock ?", listOf("Mycroft Holmes", "John Watson", "Moriarty", "Lestrade"), "Mycroft Holmes"),

        // The Big Bang Theory
        Question("Quel est le nom du personnage principal de The Big Bang Theory ?", listOf("Sheldon Cooper", "Leonard Hofstadter", "Howard Wolowitz", "Raj Koothrappali"), "Sheldon Cooper"),
        Question("Quel est le métier de Sheldon Cooper dans The Big Bang Theory ?", listOf("Physicien théoricien", "Ingénieur", "Astrophysicien", "Biologiste"), "Physicien théoricien"),
        Question("Quel est le nom de la petite amie de Leonard Hofstadter dans The Big Bang Theory ?", listOf("Penny", "Amy", "Bernadette", "Leslie"), "Penny"),

        // House of Cards
        Question("Qui est le personnage principal de House of Cards ?", listOf("Frank Underwood", "Claire Underwood", "Doug Stamper", "Zoe Barnes"), "Frank Underwood"),
        Question("Quel est le poste de Frank Underwood au début de House of Cards ?", listOf("Majority Whip", "Vice President", "President", "Secretary of State"), "Majority Whip"),
        Question("Quel est le nom de la journaliste qui enquête sur Frank Underwood dans House of Cards ?", listOf("Zoe Barnes", "Janine Skorsky", "Lucas Goodwin", "Kate Mara"), "Zoe Barnes"),

        // The Crown
        Question("Qui joue le rôle de la reine Elizabeth II dans The Crown ?", listOf("Claire Foy", "Olivia Colman", "Helena Bonham Carter", "Vanessa Kirby"), "Claire Foy"),
        Question("Quel est le nom du premier ministre britannique au début de The Crown ?", listOf("Winston Churchill", "Anthony Eden", "Harold Macmillan", "Margaret Thatcher"), "Winston Churchill"),
        Question("Quel est le nom du mari de la reine Elizabeth II dans The Crown ?", listOf("Prince Philip", "Prince Charles", "Prince Andrew", "Prince Edward"), "Prince Philip"),

        // The Witcher
        Question("Qui joue le rôle de Geralt de Riv dans The Witcher ?", listOf("Henry Cavill", "Anya Chalotra", "Freya Allan", "Joey Batey"), "Henry Cavill"),
        Question("Quel est le nom de la princesse que Geralt protège dans The Witcher ?", listOf("Ciri", "Yennefer", "Triss", "Fringilla"), "Ciri"),
        Question("Quel est le nom de la sorcière qui accompagne Geralt dans The Witcher ?", listOf("Yennefer", "Triss", "Fringilla", "Ciri"), "Yennefer"),

        // The Mandalorian
        Question("Qui est le personnage principal de The Mandalorian ?", listOf("Din Djarin", "Grogu", "Cara Dune", "Moff Gideon"), "Din Djarin"),
        Question("Quel est le nom de l'enfant que Din Djarin protège dans The Mandalorian ?", listOf("Grogu", "Baby Yoda", "The Child", "Yoda"), "Grogu"),
        Question("Quel est le nom de l'actrice qui joue Cara Dune dans The Mandalorian ?", listOf("Gina Carano", "Pedro Pascal", "Carl Weathers", "Giancarlo Esposito"), "Gina Carano"),

        // The Handmaid's Tale
        Question("Qui est le personnage principal de The Handmaid's Tale ?", listOf("June Osborne", "Serena Joy", "Aunt Lydia", "Moira"), "June Osborne"),
        Question("Quel est le nom de la société totalitaire dans The Handmaid's Tale ?", listOf("Gilead", "Mayday", "The Republic of Gilead", "The Colonies"), "Gilead"),
        Question("Quel est le nom de l'actrice qui joue June Osborne dans The Handmaid's Tale ?", listOf("Elisabeth Moss", "Yvonne Strahovski", "Ann Dowd", "Samira Wiley"), "Elisabeth Moss"),

        // Westworld
        Question("Quel est le nom du parc à thème dans Westworld ?", listOf("Westworld", "Shogunworld", "The Maze", "The Valley Beyond"), "Westworld"),
        Question("Qui joue le rôle de Dolores Abernathy dans Westworld ?", listOf("Evan Rachel Wood", "Thandie Newton", "Tessa Thompson", "Jeffrey Wright"), "Evan Rachel Wood"),
        Question("Quel est le nom du créateur du parc dans Westworld ?", listOf("Dr. Robert Ford", "Bernard Lowe", "Teddy Flood", "Maeve Millay"), "Dr. Robert Ford"),

        // Black Mirror
        Question("Quel est le nom de l'épisode de Black Mirror où les personnages sont piégés dans un jeu de réalité virtuelle ?", listOf("Playtest", "USS Callister", "San Junipero", "Nosedive"), "Playtest"),
        Question("Quel est le nom de l'épisode de Black Mirror où les personnages sont notés par les autres ?", listOf("Nosedive", "White Christmas", "The Entire History of You", "Be Right Back"), "Nosedive"),
        Question("Quel est le nom de l'épisode de Black Mirror où les personnages peuvent revivre leurs souvenirs ?", listOf("The Entire History of You", "White Christmas", "Be Right Back", "San Junipero"), "The Entire History of You"),

        // The Haunting of Hill House
        Question("Quel est le nom de la famille au centre de l'histoire dans The Haunting of Hill House ?", listOf("Crain", "Hill", "Dudley", "Jackson"), "Crain"),
        Question("Quel est le nom de la maison hantée dans The Haunting of Hill House ?", listOf("Hill House", "Bly Manor", "The Red Room", "The Bent-Neck Lady"), "Hill House"),
        Question("Quel est le nom de l'actrice qui joue Nell Crain dans The Haunting of Hill House ?", listOf("Victoria Pedretti", "Michiel Huisman", "Carla Gugino", "Elizabeth Reaser"), "Victoria Pedretti"),

        // The Haunting of Bly Manor
        Question("Quel est le nom de la maison hantée dans The Haunting of Bly Manor ?", listOf("Bly Manor", "Hill House", "The Red Room", "The Bent-Neck Lady"), "Bly Manor"),
        Question("Quel est le nom de la gouvernante dans The Haunting of Bly Manor ?", listOf("Dani Clayton", "Hannah Grose", "Jamie", "Owen"), "Dani Clayton"),
        Question("Quel est le nom de l'actrice qui joue Dani Clayton dans The Haunting of Bly Manor ?", listOf("Victoria Pedretti", "T'Nia Miller", "Amelia Eve", "Rahul Kohli"), "Victoria Pedretti"),

        // The Queen's Gambit
        Question("Qui joue le rôle de Beth Harmon dans The Queen's Gambit ?", listOf("Anya Taylor-Joy", "Moses Ingram", "Bill Camp", "Marielle Heller"), "Anya Taylor-Joy"),
        Question("Quel est le jeu auquel Beth Harmon excelle dans The Queen's Gambit ?", listOf("Échecs", "Poker", "Dames", "Go"), "Échecs"),
        Question("Quel est le nom de l'orphelinat où Beth Harmon grandit dans The Queen's Gambit ?", listOf("Methuen Home", "Beltik", "Benny", "Alma"), "Methuen Home"),

        // Bridgerton
        Question("Qui joue le rôle de Daphne Bridgerton dans Bridgerton ?", listOf("Phoebe Dynevor", "Regé-Jean Page", "Jonathan Bailey", "Nicola Coughlan"), "Phoebe Dynevor"),
        Question("Quel est le nom du duc que Daphne Bridgerton épouse dans Bridgerton ?", listOf("Simon Basset", "Anthony Bridgerton", "Colin Bridgerton", "Benedict Bridgerton"), "Simon Basset"),
        Question("Quel est le nom de la narratrice de Bridgerton ?", listOf("Lady Whistledown", "Queen Charlotte", "Eloise Bridgerton", "Penelope Featherington"), "Lady Whistledown"),

        // The Boys
        Question("Qui est le personnage principal de The Boys ?", listOf("Billy Butcher", "Hughie Campbell", "Homelander", "Starlight"), "Billy Butcher"),
        Question("Quel est le nom du groupe de super-héros dans The Boys ?", listOf("The Seven", "The Boys", "Vought International", "The Supes"), "The Seven"),
        Question("Quel est le nom de l'acteur qui joue Homelander dans The Boys ?", listOf("Antony Starr", "Karl Urban", "Jack Quaid", "Erin Moriarty"), "Antony Starr"),

        // The Umbrella Academy
        Question("Qui est le personnage principal de The Umbrella Academy ?", listOf("Number Five", "Vanya Hargreeves", "Luther Hargreeves", "Klaus Hargreeves"), "Number Five"),
        Question("Quel est le nom de la famille de super-héros dans The Umbrella Academy ?", listOf("Hargreeves", "The Umbrella Academy", "The Commission", "The Handler"), "Hargreeves"),
        Question("Quel est le nom de l'acteur qui joue Klaus Hargreeves dans The Umbrella Academy ?", listOf("Robert Sheehan", "Elliot Page", "Tom Hopper", "David Castañeda"), "Robert Sheehan"),

        // The Witcher
        Question("Qui joue le rôle de Geralt de Riv dans The Witcher ?", listOf("Henry Cavill", "Anya Chalotra", "Freya Allan", "Joey Batey"), "Henry Cavill"),
        Question("Quel est le nom de la princesse que Geralt protège dans The Witcher ?", listOf("Ciri", "Yennefer", "Triss", "Fringilla"), "Ciri"),
        Question("Quel est le nom de la sorcière qui accompagne Geralt dans The Witcher ?", listOf("Yennefer", "Triss", "Fringilla", "Ciri"), "Yennefer"),

        // The Mandalorian
        Question("Qui est le personnage principal de The Mandalorian ?", listOf("Din Djarin", "Grogu", "Cara Dune", "Moff Gideon"), "Din Djarin"),
        Question("Quel est le nom de l'enfant que Din Djarin protège dans The Mandalorian ?", listOf("Grogu", "Baby Yoda", "The Child", "Yoda"), "Grogu"),
        Question("Quel est le nom de l'actrice qui joue Cara Dune dans The Mandalorian ?", listOf("Gina Carano", "Pedro Pascal", "Carl Weathers", "Giancarlo Esposito"), "Gina Carano"),

        // The Handmaid's Tale
        Question("Qui est le personnage principal de The Handmaid's Tale ?", listOf("June Osborne", "Serena Joy", "Aunt Lydia", "Moira"), "June Osborne"),
        Question("Quel est le nom de la société totalitaire dans The Handmaid's Tale ?", listOf("Gilead", "Mayday", "The Republic of Gilead", "The Colonies"), "Gilead"),
        Question("Quel est le nom de l'actrice qui joue June Osborne dans The Handmaid's Tale ?", listOf("Elisabeth Moss", "Yvonne Strahovski", "Ann Dowd", "Samira Wiley"), "Elisabeth Moss"),

        // Westworld
        Question("Quel est le nom du parc à thème dans Westworld ?", listOf("Westworld", "Shogunworld", "The Maze", "The Valley Beyond"), "Westworld"),
        Question("Qui joue le rôle de Dolores Abernathy dans Westworld ?", listOf("Evan Rachel Wood", "Thandie Newton", "Tessa Thompson", "Jeffrey Wright"), "Evan Rachel Wood"),
        Question("Quel est le nom du créateur du parc dans Westworld ?", listOf("Dr. Robert Ford", "Bernard Lowe", "Teddy Flood", "Maeve Millay"), "Dr. Robert Ford"),

        // Black Mirror
        Question("Quel est le nom de l'épisode de Black Mirror où les personnages sont piégés dans un jeu de réalité virtuelle ?", listOf("Playtest", "USS Callister", "San Junipero", "Nosedive"), "Playtest"),
        Question("Quel est le nom de l'épisode de Black Mirror où les personnages sont notés par les autres ?", listOf("Nosedive", "White Christmas", "The Entire History of You", "Be Right Back"), "Nosedive"),
        Question("Quel est le nom de l'épisode de Black Mirror où les personnages peuvent revivre leurs souvenirs ?", listOf("The Entire History of You", "White Christmas", "Be Right Back", "San Junipero"), "The Entire History of You"),

        // The Haunting of Hill House
        Question("Quel est le nom de la famille au centre de l'histoire dans The Haunting of Hill House ?", listOf("Crain", "Hill", "Dudley", "Jackson"), "Crain"),
        Question("Quel est le nom de la maison hantée dans The Haunting of Hill House ?", listOf("Hill House", "Bly Manor", "The Red Room", "The Bent-Neck Lady"), "Hill House"),
        Question("Quel est le nom de l'actrice qui joue Nell Crain dans The Haunting of Hill House ?", listOf("Victoria Pedretti", "Michiel Huisman", "Carla Gugino", "Elizabeth Reaser"), "Victoria Pedretti"),

        // The Haunting of Bly Manor
        Question("Quel est le nom de la maison hantée dans The Haunting of Bly Manor ?", listOf("Bly Manor", "Hill House", "The Red Room", "The Bent-Neck Lady"), "Bly Manor"),
        Question("Quel est le nom de la gouvernante dans The Haunting of Bly Manor ?", listOf("Dani Clayton", "Hannah Grose", "Jamie", "Owen"), "Dani Clayton"),
        Question("Quel est le nom de l'actrice qui joue Dani Clayton dans The Haunting of Bly Manor ?", listOf("Victoria Pedretti", "T'Nia Miller", "Amelia Eve", "Rahul Kohli"), "Victoria Pedretti"),

        // The Queen's Gambit
        Question("Qui joue le rôle de Beth Harmon dans The Queen's Gambit ?", listOf("Anya Taylor-Joy", "Moses Ingram", "Bill Camp", "Marielle Heller"), "Anya Taylor-Joy"),
        Question("Quel est le jeu auquel Beth Harmon excelle dans The Queen's Gambit ?", listOf("Échecs", "Poker", "Dames", "Go"), "Échecs"),
        Question("Quel est le nom de l'orphelinat où Beth Harmon grandit dans The Queen's Gambit ?", listOf("Methuen Home", "Beltik", "Benny", "Alma"), "Methuen Home"),

        // Bridgerton
        Question("Qui joue le rôle de Daphne Bridgerton dans Bridgerton ?", listOf("Phoebe Dynevor", "Regé-Jean Page", "Jonathan Bailey", "Nicola Coughlan"), "Phoebe Dynevor"),
        Question("Quel est le nom du duc que Daphne Bridgerton épouse dans Bridgerton ?", listOf("Simon Basset", "Anthony Bridgerton", "Colin Bridgerton", "Benedict Bridgerton"), "Simon Basset"),
        Question("Quel est le nom de la narratrice de Bridgerton ?", listOf("Lady Whistledown", "Queen Charlotte", "Eloise Bridgerton", "Penelope Featherington"), "Lady Whistledown"),

        // The Boys
        Question("Qui est le personnage principal de The Boys ?", listOf("Billy Butcher", "Hughie Campbell", "Homelander", "Starlight"), "Billy Butcher"),
        Question("Quel est le nom du groupe de super-héros dans The Boys ?", listOf("The Seven", "The Boys", "Vought International", "The Supes"), "The Seven"),
        Question("Quel est le nom de l'acteur qui joue Homelander dans The Boys ?", listOf("Antony Starr", "Karl Urban", "Jack Quaid", "Erin Moriarty"), "Antony Starr"),

        // The Umbrella Academy
        Question("Qui est le personnage principal de The Umbrella Academy ?", listOf("Number Five", "Vanya Hargreeves", "Luther Hargreeves", "Klaus Hargreeves"), "Number Five"),
        Question("Quel est le nom de la famille de super-héros dans The Umbrella Academy ?", listOf("Hargreeves", "The Umbrella Academy", "The Commission", "The Handler"), "Hargreeves"),
        Question("Quel est le nom de l'acteur qui joue Klaus Hargreeves dans The Umbrella Academy ?", listOf("Robert Sheehan", "Elliot Page", "Tom Hopper", "David Castañeda"), "Robert Sheehan")
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)

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