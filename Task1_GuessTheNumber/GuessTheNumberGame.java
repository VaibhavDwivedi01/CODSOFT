import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GuessTheNumberGame extends JFrame {

    private int targetNumber;
    private int attemptsLeft = 10;
    private int score = 0;
    private int round = 1;

    private JLabel instructionLabel, messageLabel, attemptsLabel, scoreLabel;
    private JTextField guessField;
    private JButton submitButton, resetButton, exitButton;

    public GuessTheNumberGame() {
        setTitle("Guess The Number Game");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        instructionLabel = new JLabel("Guess a number between 1 and 100", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 18));

        messageLabel = new JLabel("You have 10 attempts", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        messageLabel.setForeground(new Color(0, 102, 204));

        topPanel.add(instructionLabel);
        topPanel.add(messageLabel);

        // Input Panel
        JPanel inputPanel = new JPanel();
        guessField = new JTextField(10);
        guessField.setFont(new Font("Arial", Font.PLAIN, 16));

        submitButton = new JButton("Submit Guess");
        resetButton = new JButton("Play Again");
        exitButton = new JButton("Exit Game");

        inputPanel.add(new JLabel("Your Guess: "));
        inputPanel.add(guessField);
        inputPanel.add(submitButton);
        inputPanel.add(resetButton);
        inputPanel.add(exitButton);

        // Status Panel
        JPanel statusPanel = new JPanel(new GridLayout(2, 1));
        attemptsLabel = new JLabel("Attempts left: 10", SwingConstants.CENTER);
        scoreLabel = new JLabel("Score: 0 | Round: 1", SwingConstants.CENTER);

        attemptsLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
        scoreLabel.setFont(new Font("Verdana", Font.PLAIN, 14));

        statusPanel.add(attemptsLabel);
        statusPanel.add(scoreLabel);

        // Assemble
        add(topPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        resetButton.setEnabled(false);

        generateRandomNumber();

        submitButton.addActionListener(e -> checkGuess());
        resetButton.addActionListener(e -> resetGame());
        exitButton.addActionListener(e -> System.exit(0));
    }

    private void generateRandomNumber() {
        Random random = new Random();
        targetNumber = random.nextInt(100) + 1;
    }

    private void checkGuess() {
        String input = guessField.getText().trim();
        int guess;

        try {
            guess = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter a valid number.");
            messageLabel.setForeground(Color.RED);
            return;
        }

        if (guess < 1 || guess > 100) {
            messageLabel.setText("Number must be between 1 and 100.");
            messageLabel.setForeground(Color.RED);
            return;
        }

        if (guess == targetNumber) {
            messageLabel.setText("Correct! You guessed it!");
            messageLabel.setForeground(new Color(0, 153, 51));
            score += attemptsLeft * 10;
            endRound();
        } else {
            attemptsLeft--;

            int diff = Math.abs(guess - targetNumber);
            if (diff <= 5) {
                messageLabel.setText("Very close! Try again.");
                messageLabel.setForeground(new Color(255, 102, 0));
            } else if (guess < targetNumber) {
                messageLabel.setText("Too low! Try again.");
                messageLabel.setForeground(Color.BLUE);
            } else {
                messageLabel.setText("Too high! Try again.");
                messageLabel.setForeground(Color.BLUE);
            }

            if (attemptsLeft == 0) {
                messageLabel.setText("Out of attempts! Number was " + targetNumber);
                messageLabel.setForeground(Color.RED);
                endRound();
            }

            attemptsLabel.setText("Attempts left: " + attemptsLeft);
            guessField.setText("");
        }
    }

    private void endRound() {
        submitButton.setEnabled(false);
        resetButton.setEnabled(true);
        scoreLabel.setText("Score: " + score + " | Round: " + round);
    }

    private void resetGame() {
        round++;
        attemptsLeft = 10;
        generateRandomNumber();
        messageLabel.setText("New round! Guess a number.");
        messageLabel.setForeground(new Color(0, 102, 204));
        attemptsLabel.setText("Attempts left: 10");
        scoreLabel.setText("Score: " + score + " | Round: " + round);
        submitButton.setEnabled(true);
        resetButton.setEnabled(false);
        guessField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuessTheNumberGame game = new GuessTheNumberGame();
            game.setVisible(true);
        });
    }
}
