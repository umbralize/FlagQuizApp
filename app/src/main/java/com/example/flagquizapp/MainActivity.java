package com.example.flagquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private String[] correctAnswers = {"USA", "Germany", "France", "Canada"};
    private String[] allCountries = {
            "USA", "Germany", "France", "Canada", "Brazil", "Japan", "Mexico", "India", "Italy", "Spain"
    };

    private int currentQuestionIndex = 0;
    private int score = 0;
    private boolean hintUsed = false; // Tracks if hint is used

    private ImageView flagImageView;
    private Button option1, option2, option3, option4;
    private TextView scoreTextView;
    private Button hintButton; // Hint button

    private String userName; // Store user's name
    private ScoreDatabaseHelper dbHelper; // SQLite helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get user name from Intent
        Intent intent = getIntent();
        userName = intent.getStringExtra("USER_NAME");

        flagImageView = findViewById(R.id.flag_image);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        scoreTextView = findViewById(R.id.score_text);
        hintButton = findViewById(R.id.hint_button); // Link the hint button

        dbHelper = new ScoreDatabaseHelper(this); // Initialize the database

        // Load the first question
        loadNextQuestion();

        // Set up the button click listeners
        option1.setOnClickListener(view -> checkAnswer(option1.getText().toString()));

        option2.setOnClickListener(view -> checkAnswer(option2.getText().toString()));

        option3.setOnClickListener(view -> checkAnswer(option3.getText().toString()));

        option4.setOnClickListener(view -> checkAnswer(option4.getText().toString()));

        // Hint button click listener
        hintButton.setOnClickListener(view -> {
            if (!hintUsed) {
                provideHint();
                hintUsed = true; // Mark hint as used
                hintButton.setEnabled(false); // Disable hint after use
            } else {
                Toast.makeText(MainActivity.this, "Hint already used!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < correctAnswers.length) {
            // Reset button visibility before loading the new question
            resetButtonVisibility();

            String currentCountry = correctAnswers[currentQuestionIndex].toLowerCase().replace(" ", "_");
            int resID = getResources().getIdentifier("flag_" + currentCountry, "drawable", getPackageName());
            flagImageView.setImageResource(resID);

            List<String> options = generateRandomOptions(correctAnswers[currentQuestionIndex]);

            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));
            option4.setText(options.get(3));

            scoreTextView.setText("Score: " + score);

            // Reset hint for the next question
            hintUsed = false;
            hintButton.setEnabled(true); // Re-enable the hint button
        } else {
            // Store score in SQLite database
            dbHelper.addScore(userName, score);

            // End of quiz, show scoreboard
            Intent intent = new Intent(MainActivity.this, ScoreboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private List<String> generateRandomOptions(String correctAnswer) {
        List<String> options = new ArrayList<>();
        Random random = new Random();

        options.add(correctAnswer);
        while (options.size() < 4) {
            String randomCountry = allCountries[random.nextInt(allCountries.length)];
            if (!options.contains(randomCountry)) {
                options.add(randomCountry);
            }
        }

        Collections.shuffle(options);
        return options;
    }

    private void checkAnswer(String selectedAnswer) {
        if (selectedAnswer.equals(correctAnswers[currentQuestionIndex])) {
            score++;
        }
        currentQuestionIndex++;
        loadNextQuestion();
    }

    // Hint logic: removes two wrong options
    private void provideHint() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(option1);
        buttons.add(option2);
        buttons.add(option3);
        buttons.add(option4);

        // Find the button with the correct answer
        Button correctButton = null;
        for (Button button : buttons) {
            if (button.getText().toString().equals(correctAnswers[currentQuestionIndex])) {
                correctButton = button;
                break;
            }
        }

        // Remove two incorrect answers
        int removed = 0;
        for (Button button : buttons) {
            if (!button.equals(correctButton) && removed < 2) {
                button.setVisibility(View.INVISIBLE); // Hide wrong answers
                removed++;
            }
        }
    }

    // Method to reset the visibility of all buttons
    private void resetButtonVisibility() {
        option1.setVisibility(View.VISIBLE);
        option2.setVisibility(View.VISIBLE);
        option3.setVisibility(View.VISIBLE);
        option4.setVisibility(View.VISIBLE);
    }
}
