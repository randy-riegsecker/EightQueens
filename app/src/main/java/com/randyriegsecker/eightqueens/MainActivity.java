package com.randyriegsecker.eightqueens;

// Author: Randy Riegsecker
// Date: 2022/12/31
// Eight Queens Game main activity
// This is the title screen for the first activity
// https://github.com/randy-riegsecker/EightQueens

// Thanks to Practical Coding - https://www.youtube.com/@PracticalCoding
// and his TicTacToe game design walk through tutorial for ideas
// https://youtube.com/watch?v=Fa5egLurW5U&si=EnSIkaIECMiOmarE

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupHyperlink();
    }

    public void startButtonClick(View view) {
        Intent intent = new Intent(this, GameDisplay.class );
        startActivity(intent);
    }

    private void setupHyperlink() {
        TextView linkTextView = findViewById(R.id.githubURL);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}