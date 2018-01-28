package com.home.graham.robotcontroller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class TrailBlazer extends AppCompatActivity {

    Square[][] squares;
    GridLayout gameGrid;

    Button resetButton;
    Button runButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_blazer);

        // Reset squares on game grid
        Square.activeSquares = new ArrayList<>();

        resetButton = findViewById(R.id.reset_button);
        runButton = findViewById(R.id.run_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TrailBlazer.this, TrailBlazer.class));
            }
        });

        runButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                for (Square square : Square.activeSquares) {
                    MainActivity.queue.add(square.toString());
                }
            }
        });

        gameGrid = findViewById(R.id.path_grid);
        final int numCol = gameGrid.getColumnCount();
        final int numRow = gameGrid.getRowCount();
        squares = new Square[numRow][numCol];
        for (int x=0; x<numRow; x++) {
            for (int y=0; y < numCol; y++) {
                Square square;
                gameGrid.addView((squares[x][y] = (square = new Square(this, y, x))));
                if ((y == (numCol)/2) && (x == (numRow)/2)) {
                    square.active = true;
                    Square.activeSquares.add(square);
                }
            }
        }

        gameGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int MARGIN = 2;
                int squareWidth = (gameGrid.getWidth()/numCol) - (2*MARGIN);
                int squareHeight = (gameGrid.getHeight()/numRow) - (2*MARGIN);
                for (int x=0; x<numRow; x++) {
                    for (int y=0; y<numCol; y++) {
                        Square square;
                        GridLayout.LayoutParams params = (GridLayout.LayoutParams)(square = squares[x][y]).getLayoutParams();
                        params.width = squareWidth;
                        params.height = squareWidth;
                        params.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
                        square.setLayoutParams(params);
                    }
                }
            }
        });

        gameGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                for (int i=0; i<gameGrid.getChildCount(); i++) {
                    Square square = (Square) gameGrid.getChildAt(i);
                    Rect bounds = new Rect(square.getLeft(), square.getTop(), square.getRight(), square.getBottom());
                    if (bounds.contains((int)event.getX(), (int)event.getY())) {
                        // Ensure we are not moving back onto the same square we just came from.
                        if (Square.activeSquares.size() == 1 || !Square.activeSquares.get(Square.activeSquares.size()-2).equals(this)) {
                            Square lastSquare = Square.activeSquares.get(Square.activeSquares.size() - 1);
                            int lastX = lastSquare.x;
                            int lastY = lastSquare.y;
                            // Finally, ensure that we have tapped on a square which borders the last highlighted square.
                            if ((Math.abs(lastX - square.x) == 1 && square.y == lastY) || (Math.abs(lastY - square.y) == 1 && square.x == lastX)) {
                                square.active = true;
                                Square.activeSquares.add(square);
                                square.setBackgroundColor(getResources().getColor(R.color.selectedSquare));
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        });
    }
}
