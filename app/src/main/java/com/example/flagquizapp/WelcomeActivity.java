package com.example.flagquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private EditText nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        nameInput = findViewById(R.id.name_input);
        Button startButton = findViewById(R.id.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = nameInput.getText().toString();
                if (!userName.isEmpty()) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    intent.putExtra("USER_NAME", userName);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
