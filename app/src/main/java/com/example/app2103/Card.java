package com.example.app2103;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Card {

    Paint p = new Paint();

    public Card(float x, float y, float width, float height, int color) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    int color, backColor = Color.parseColor("#455a64");
    float x, y, width, height;
    boolean isOpen = false;


    public boolean flip(float touchX, float touchY) {
        if (touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height) {
            isOpen = !isOpen;
            return true;
        } else return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void draw(Canvas c) {
        //нарисовать карту в виде прямоугольника
        if (isOpen) {
            p.setColor(color);
        } else {
            p.setColor(backColor);
        }

        //c.drawRect(x, y, x + width, y + height, p);
        c.drawRoundRect(x, y, x + width, y + height, 25, 25,p);
    }


}
