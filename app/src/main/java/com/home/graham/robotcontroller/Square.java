package com.home.graham.robotcontroller;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Graham Home on 1/27/2018.
 */

public class Square extends View {

    public static ArrayList<Square> activeSquares = new ArrayList<>();

    public boolean active;
    public int x = 0;
    public int y = 0;

    public Square(Context context, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        init();
    }

    public void init() {
        active = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(active ? getResources().getColor(R.color.selectedSquare) : getResources().getColor(R.color.unselectedSquare));
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        // First, ensure we have just tapped this square.
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
            // Next, ensure we are not moving back onto the same square we just came from.
            if (activeSquares.size() == 1 || !activeSquares.get(activeSquares.size()-2).equals(this)) {
                Square lastSquare = activeSquares.get(activeSquares.size() - 1);
                int lastX = lastSquare.x;
                int lastY = lastSquare.y;
                // Finally, ensure that we have tapped on a square which borders the last highlighted square.
                if ((Math.abs(lastX - x) == 1 && y == lastY) || (Math.abs(lastY - y) == 1 && x == lastX)) {
                    this.active = true;
                    activeSquares.add(this);
                    this.setBackgroundColor(getResources().getColor(R.color.selectedSquare));
                    return true;
                }
            }
        }
        return false;
    }*/
    @Override
    public String toString() {
        return this.x + "," + this.y;
    }
}
