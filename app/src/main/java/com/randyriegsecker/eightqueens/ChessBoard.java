package com.randyriegsecker.eightqueens;

// Author: Randy Riegsecker
// Date: 2022/12/31
// Eight Queens Game
// This is the chess board drawing
// https://github.com/randy-riegsecker/EightQueens

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ChessBoard extends View {

    final int boardColor;
    final int lightSquare;
    final int darkSquare;
    final int redSquare;
    final int greenSquare;
    final int chessPiece;

    int cellSize;
    int boardSize;

    // Background and margin around board
    final int boardMargin = 16;
    final int rectMargin = 5;

    public Bitmap whiteQueen;
    public Bitmap blackQueen;
    public Bitmap scaledWhiteQueen;
    public Bitmap scaledBlackQueen;

    private final Paint paint = new Paint();

    private final GameLogic game;

    public ChessBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        game = new GameLogic();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ChessBoard, 0, 0);

        try {
            boardColor = a.getInteger(R.styleable.ChessBoard_boardColor, 0);
            lightSquare = a.getInteger(R.styleable.ChessBoard_lightSquare, 0);
            darkSquare = a.getInteger(R.styleable.ChessBoard_darkSquare, 0);
            redSquare = a.getInteger(R.styleable.ChessBoard_redSquare, 0);
            greenSquare = a.getInteger(R.styleable.ChessBoard_greenSquare, 0);
            chessPiece = a.getInteger(R.styleable.ChessBoard_chessPiece, 0);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);

        // find minimum size to make perfectly square board
        boardSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
        cellSize = boardSize / 8;
        setMeasuredDimension(boardSize, boardSize);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            // calculate 2d array position and make sure it's in range
            int row = (int) Math.ceil((y - boardMargin) / cellSize);
            int col = (int) Math.ceil((x - boardMargin) / cellSize);

            // clicked on boardMargin - ignore it
            if (row > 8 || row < 1 || col > 8 || col < 1) {
                return false;
            }

            if (!game.checkWinner()) {
                if (game.updateGameBoard(row, col)) {
                    invalidate();
                }
            }

            if (game.checkWinner()) {
                game.winnerMessage();
            }
            return false;
        }
        return false;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawChessBoard(canvas);
        drawChessPieces(canvas);

        // for debugging directly drew queens
        // drawQueen(canvas, 5,5);




    }

    private void drawChessBoard(Canvas canvas) {

//        paint.setStyle(Paint.Style.FILL);
//        paint.setAntiAlias(true);
//        paint.setStrokeWidth(25);

        // fill the background
        paint.setColor(boardColor);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(0, 0, boardSize, boardSize, paint);

        cellSize = (boardSize - (boardMargin * 2)) / 8;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                // even row/col or odd row/col = lightSquare
                if (((r % 2 == 0) && (c % 2 == 0)) || ((r % 2 == 1) && (c % 2 == 1)))
                    paint.setColor(lightSquare);
                else
                    paint.setColor(darkSquare);

                canvas.drawRect(c * cellSize + boardMargin,
                        r * cellSize + boardMargin,
                        c * cellSize + cellSize + boardMargin,
                        r * cellSize + cellSize + boardMargin,
                        paint);
            }
        }

        // black Queen in case you prefer to use it!
        blackQueen = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.queen_black);
        scaledBlackQueen = Bitmap.createScaledBitmap(blackQueen, (int) (cellSize * .9), (int) (cellSize * .9), true);

        whiteQueen = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.queen_white);
        scaledWhiteQueen = Bitmap.createScaledBitmap(whiteQueen, (int) (cellSize * .9), (int) (cellSize * .9), true);
    }

    // draw chess pieces where you've picked
    private void drawChessPieces(Canvas canvas) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (game.getGameBoard()[r][c] != 0) {
                    drawQueen(canvas, r, c);
                }
            }
        }
    }

    private void drawQueen(Canvas canvas, int row, int col) {

//        If you want to draw circles instead of queens, this code does that.
//        paint.setColor(chessPiece);
//        float chessPieceMargin = (float) 0.1;
//        // left, top, right, bottom
//        canvas.drawOval((float) (col * cellSize + boardMargin + cellSize * chessPieceMargin),
//                        (float) (row * cellSize + boardMargin + cellSize * chessPieceMargin),
//                        (float) (col * cellSize + cellSize + boardMargin - cellSize * chessPieceMargin),
//                        (float) (row * cellSize + cellSize + boardMargin - cellSize * chessPieceMargin),
//                        paint);

        // if conflict make square pink before drawing queen
        if (game.isAttack(row,col) == true) {
            paint.setColor(redSquare);

            paint.setStyle(Paint.Style.FILL);
            // paint.setStrokeWidth(6);
            canvas.drawRect(col * cellSize + boardMargin + rectMargin ,
                    row * cellSize + boardMargin + rectMargin,
                    col * cellSize + cellSize + boardMargin - rectMargin,
                    row * cellSize + cellSize + boardMargin - rectMargin,
                    paint);
        }

        canvas.drawBitmap(scaledWhiteQueen, (float) (col * cellSize + boardMargin + cellSize * .05),
                (float) (row * cellSize + boardMargin + cellSize * .05), paint);

        // if you won, make all the covered squares green!
        if (game.isPuzzleSolved() == true) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(greenSquare);
            canvas.drawRect(col * cellSize + boardMargin + 6 ,
                    row * cellSize + boardMargin + 6,
                    col * cellSize + cellSize + boardMargin - 6,
                    row * cellSize + cellSize + boardMargin - 6,
                    paint);
            canvas.drawBitmap(scaledWhiteQueen, (float) (col * cellSize + boardMargin + cellSize * .05),
                    (float) (row * cellSize + boardMargin + cellSize * .05), paint);
        }
    }

    public void setUpGame(TextView queenCount) {
        game.setQueenCount(queenCount);
    }

    public void clearBoard() {
        game.clearBoard();
    }

    public void resetCorrectAnswer() {
        game.setPuzzleSolved(false);
    }
}

