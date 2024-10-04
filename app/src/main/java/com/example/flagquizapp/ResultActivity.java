package com.example.flagquizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView scoreTextView = findViewById(R.id.final_score_text);
        TextView highScoreTextView = findViewById(R.id.high_score_text);
        Button tryAgainButton = findViewById(R.id.try_again_button);

        int score = getIntent().getIntExtra("SCORE", 0);

        scoreTextView.setText("Your Score: " + score);

        SharedPreferences sharedPreferences = getSharedPreferences("QUIZ_APP", MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.apply();
            highScoreTextView.setText("New High Score: " + score);
        } else {
            highScoreTextView.setText("High Score: " + highScore);
        }

        // Set up the Try Again button to restart the quiz
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Restart the quiz by starting MainActivity
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  // Close ResultActivity so that it doesn't remain in the activity stack
            }
        });
    }
}
