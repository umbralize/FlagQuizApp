package com.example.flagquizapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardActivity extends AppCompatActivity {

    private ListView scoreListView;
    private ScoreDatabaseHelper dbHelper;
    private Button tryAgainButton;
    private Button clearScoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoreListView = findViewById(R.id.score_list_view);
        tryAgainButton = findViewById(R.id.try_again_button);
        clearScoresButton = findViewById(R.id.clear_scores_button);
        dbHelper = new ScoreDatabaseHelper(this);

        loadScores();

        // "Try Again" button to restart the quiz
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreboardActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // "Clear Scores" button to delete all scores
        clearScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteAllScores(); // Call method to delete all scores
                loadScores(); // Refresh the scoreboard
            }
        });
    }

    private void loadScores() {
        List<String> scores = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, score FROM scores ORDER BY score DESC", null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                int score = cursor.getInt(1);
                scores.add(name + ": " + score);
            } while (cursor.moveToNext());
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, scores);
        scoreListView.setAdapter(adapter);
    }
}
