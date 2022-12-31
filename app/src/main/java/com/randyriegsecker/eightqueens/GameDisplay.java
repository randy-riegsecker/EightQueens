package com.randyriegsecker.eightqueens;

// Author: Randy Riegsecker
// Date: 2022/12/31
// Eight Queens Game board activity
// https://github.com/randy-riegsecker/EightQueens

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameDisplay extends AppCompatActivity {

    private ChessBoard chessBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_board);

        chessBoard = findViewById(R.id.chessBoard);

        TextView queenCount = findViewById(R.id.queenCount);

        chessBoard.setUpGame(queenCount);
    }

    public void clearBoardButtonClick(View view) {
        chessBoard.clearBoard();
        chessBoard.invalidate();
    }
}