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
        canvas.drawColor(active ? getResources().getColor(R.color.selectedSquare) : getResources().getColor(R.color.inactiveView));
    }

    @Override
    public String toString() {
        return this.x + "," + this.y;
    }
}
