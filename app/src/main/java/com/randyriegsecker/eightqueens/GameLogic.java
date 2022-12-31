package com.randyriegsecker.eightqueens;

// Author: Randy Riegsecker
// Date: 2022/12/31
// Eight Queens Game game logic
// This is the title screen for the first activity
// https://github.com/randy-riegsecker/EightQueens

import android.util.Log;
import android.widget.TextView;

public class GameLogic {

    private int[][] gameBoard;
    private boolean[][] conflictBoard;
    public int queensOnBoard = 0;

    private TextView queenCount;
    private boolean returnVal;

    private boolean puzzleSolved = false;

    public GameLogic() {
        gameBoard = new int[8][8];
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                gameBoard[r][c] = 0;
            }
        }
    }

    public boolean updateGameBoard(int row, int col) {

        if (gameBoard[row - 1][col - 1] == 0 && queensOnBoard < 8) {
            gameBoard[row - 1][col - 1] = 1;
            ++queensOnBoard;
        } else if (gameBoard[row - 1][col - 1] == 1) {
            gameBoard[row - 1][col - 1] = 0;
            --queensOnBoard;
        }
        queenCount.setText("Queens Placed: " + String.valueOf(queensOnBoard));

        // check for attack positions
        invalidPlacement();

        return true;
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void clearBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                gameBoard[r][c] = 0;
                // conflictBoard[r][c] = false;
            }
        }

        queensOnBoard = 0;
        queenCount.setText("Queens Placed: 0");
    }

    public void setQueenCount(TextView queenCount) {
        this.queenCount = queenCount;
    }

    public boolean invalidPlacement() {
        // check for conflict make attacked true;
        // also used to return if game is over

        returnVal = false;

        int r, rD, c, cD;

        // reset conflict board
        conflictBoard = new boolean[8][8];
        for (r = 0; r < 8; r++) {
            for (c = 0; c < 8; c++) {
                conflictBoard[r][c] = false;
            }
        }

        // check for invalid positions
        // This is nightmarish and inefficient brute force code but gets the job done.
        for (c = 0; c < 8; c++) {
            for (r = 0; r < 8; r++) {
                for (cD = 0; cD < 8; cD++) {
                    for (rD = 0; rD < 8; rD++) {
                        // If queen can attack diagonally
                        if (gameBoard[r][c] == 1 && gameBoard[rD][cD] == 1 && (Math.abs(r - rD) == Math.abs(c - cD)) && r != rD && c != cD) {
                            conflictBoard[r][c] = true;
                            returnVal = true;
                        }

                        // rows
                        if ((c != cD) && ((gameBoard[r][c] == 1) && (gameBoard[r][cD] == 1))) {
                            conflictBoard[r][c] = true;
                            returnVal = true;
                        }

                        // cols
                        if ((r != rD) && ((gameBoard[r][c] == 1) && (gameBoard[rD][c] == 1))) {
                           conflictBoard[r][c] = true;
                           returnVal = true;
                        }
                    }
                }
            }
        }
        return returnVal;
    }

    public boolean[][] getConflictBoard() {
        return conflictBoard;
    }

    public boolean isAttack(int row, int col) {
        return conflictBoard[row][col];
    }

    public boolean checkWinner() {

        if (queensOnBoard == 8 && invalidPlacement() == false) {
            Log.d("EightQueens", "game.checkWinner TRUE.");
            puzzleSolved = true;
            return true;
        } else {
            Log.d("EightQueens", "game.checkWinner FALSE.");
            puzzleSolved = false;
            return false;
        }
    }

    public void winnerMessage() {
        queenCount.setText("You Solved It!");
    }

    public boolean isPuzzleSolved() {
        return puzzleSolved;
    }

    public void setPuzzleSolved(boolean puzzleSolved) {
        this.puzzleSolved = puzzleSolved;
    }

    public int getQueensOnBoard() {
        return queensOnBoard;
    }
}

