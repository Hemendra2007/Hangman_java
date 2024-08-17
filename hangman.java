import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class Hangman {

    private static final String[] EASY_WORDS = {"java", "code", "debug"};
    private static final String[] MEDIUM_WORDS = {"hangman", "python", "arrays"};
    private static final String[] HARD_WORDS = {"programming", "development", "exception"};
    
    private static String[] currentWordList;
    private static String wordToGuess;
    private static StringBuilder guessedWord;
    private static Set<Character> guessedLetters;
    private static Set<Character> incorrectGuesses;
    private static int attemptsRemaining;
    private static int gamesWon = 0;
    private static int gamesLost = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        System.out.println("Welcome to Hangman!");

        while (playAgain) {
            chooseDifficulty(scanner);

            initializeGame();

            while (attemptsRemaining > 0 && !guessedWord.toString().equals(wordToGuess)) {
                displayGameStatus();
                System.out.print("Enter a letter (or type 'hint' for a hint): ");
                String input = scanner.next().toLowerCase();

                if (input.equals("hint")) {
                    provideHint();
                } else if (input.length() == 1) {
                    char guess = input.charAt(0);
                    if (processGuess(guess)) {
                        System.out.println("Correct guess!");
                    } else {
                        System.out.println("Incorrect guess.");
                        attemptsRemaining--;
                    }
                } else {
                    System.out.println("Please enter a single letter.");
                }
            }

            concludeGame();

            System.out.print("Do you want to play again? (yes/no): ");
            String response = scanner.next().toLowerCase();
            playAgain = response.equals("yes");
        }

        System.out.println("Thank you for playing! Final score:");
        System.out.println("Games won: " + gamesWon);
        System.out.println("Games lost: " + gamesLost);
        scanner.close();
    }

    private static void chooseDifficulty(Scanner scanner) {
        System.out.println("Choose difficulty level:");
        System.out.println("1. Easy");
        System.out.println("2. Medium");
        System.out.println("3. Hard");
        System.out.print("Enter your choice (1, 2, or 3): ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                currentWordList = EASY_WORDS;
                attemptsRemaining = 8; // More attempts for easier words
                break;
            case 2:
                currentWordList = MEDIUM_WORDS;
                attemptsRemaining = 6;
                break;
            case 3:
                currentWordList = HARD_WORDS;
                attemptsRemaining = 4; // Fewer attempts for harder words
                break;
            default:
                System.out.println("Invalid choice. Defaulting to Easy.");
                currentWordList = EASY_WORDS;
                attemptsRemaining = 8;
                break;
        }
    }

    private static void initializeGame() {
        Random random = new Random();
        int index = random.nextInt(currentWordList.length);
        wordToGuess = currentWordList[index];
        guessedWord = new StringBuilder("_".repeat(wordToGuess.length()));
        guessedLetters = new HashSet<>();
        incorrectGuesses = new HashSet<>();
    }

    private static void displayGameStatus() {
        System.out.println("Word to guess: " + guessedWord);
        System.out.println("Incorrect guesses: " + incorrectGuesses);
        System.out.println("Attempts remaining: " + attemptsRemaining);
    }

    private static boolean processGuess(char guess) {
        if (!isValidInput(guess)) {
            System.out.println("Invalid or repeated guess. Try again.");
            return false;
        }

        boolean correctGuess = false;
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (wordToGuess.charAt(i) == guess) {
                guessedWord.setCharAt(i, guess);
                correctGuess = true;
            }
        }
        if (!correctGuess) {
            incorrectGuesses.add(guess);
        } else {
            guessedLetters.add(guess);
        }
        return correctGuess;
    }

    private static boolean isValidInput(char guess) {
        return Character.isLetter(guess) && !guessedLetters.contains(guess) && !incorrectGuesses.contains(guess);
    }

    private static void provideHint() {
        Set<Character> remainingLetters = new HashSet<>();
        for (char c : wordToGuess.toCharArray()) {
            if (!guessedLetters.contains(c) && !incorrectGuesses.contains(c)) {
                remainingLetters.add(c);
            }
        }
        if (!remainingLetters.isEmpty()) {
            char hintLetter = remainingLetters.iterator().next();
            System.out.println("Hint: The word contains the letter '" + hintLetter + "'.");
        } else {
            System.out.println("No hints available.");
        }
    }

    private static void concludeGame() {
        if (guessedWord.toString().equals(wordToGuess)) {
            System.out.println("Congratulations! You've guessed the word: " + wordToGuess);
            gamesWon++;
        } else {
            System.out.println("Game over! The word was: " + wordToGuess);
            gamesLost++;
        }

        System.out.println("Games won: " + gamesWon);
        System.out.println("Games lost: " + gamesLost);
    }
}
