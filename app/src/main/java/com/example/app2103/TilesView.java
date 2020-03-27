package com.example.app2103;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class TilesView extends View {

    final int PAUSE_LENGTH = 1;
    int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    int openedCards = 0;

    float tmpWidth = displayWidth / 5;
    float tmpHeight = displayWidth / 5;
    float tmpX = tmpWidth / 3;
    float tmpY = tmpWidth / 3;

    ArrayList<Card> cards = new ArrayList<>();
    List<Integer> colors;

    boolean isOnPauseNow = false;

    Card currentCard = null;


    public TilesView(Context context) {
        super(context);
    }

    public TilesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setColorsAndTiles();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Card c : cards) {
            c.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN && !isOnPauseNow) {

            for (Card c : cards) {

                if (openedCards == 0) {

                    if (c.flip(x, y)) {
                        currentCard = c;
                        openedCards++;
                        invalidate();
                        return true;
                    }

                }
                if (openedCards == 1) {

                    if (c.flip(x, y)) {

                        if (currentCard.equals(c)) {
                            openedCards = 0;
                            invalidate();
                            return true;
                        }


                        openedCards++;
                        if (checkOpenCardsEqual()) {
                            openedCards = 0;
                            invalidate();
                            return true;
                        }
                        invalidate();
                        PauseTask task = new PauseTask();
                        task.execute(PAUSE_LENGTH);
                        isOnPauseNow = true;
                        return true;


                    }
                }
            }
        }

        invalidate();
        return true;
    }

    class PauseTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            Log.d("mytag", "Pause started");
            try {
                Thread.sleep(integers[0] * 1000); // передаем число секунд ожидания
            } catch (InterruptedException e) {

            }
            Log.d("mytag", "Pause finished");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            for (Card c : cards) {
                if (c.isOpen) {
                    c.isOpen = false;
                }
            }
            openedCards = 0;
            isOnPauseNow = false;
            invalidate();
        }
    }

    public boolean checkOpenCardsEqual() {

        int color = Color.rgb(0, 0, 0);
        Card firstCard = cards.get(0);
        Card secondCard;

        int cnt = 0;
        boolean tmp = true;
        for (int i = 0; tmp; i++) {
            if (cards.get(i).isOpen) {
                color = cards.get(i).color;
                cnt = i;
                tmp = false;
                firstCard = cards.get(i);
            }

        }

        for (int i = cnt + 1; i < cards.size(); i++) {
            if (cards.get(i).isOpen && cards.get(i).color == color) {

                secondCard = cards.get(i);
                Log.d("myTag", "checkOpenCardsEqual: " + firstCard + " " + secondCard + getWidth());
                Log.d("myTag", "checkOpenCardsEqual: " + firstCard.color + " " + secondCard.color + getHeight());
                cards.remove(firstCard);
                cards.remove(secondCard);

                if (cards.size() == 0) {
                    Toast toast = Toast.makeText(getContext(),
                            "Победа!", Toast.LENGTH_SHORT);
                    toast.show();
                    setColorsAndTiles();
                }
                return true;

            }
        }


        return false;

    }

    public void setColorsAndTiles() {
        colors = Arrays.asList(
                getResources().getColor(R.color.tileColorK), getResources().getColor(R.color.tileColorL),
                getResources().getColor(R.color.tileColorM), getResources().getColor(R.color.tileColorN),
                getResources().getColor(R.color.tileColorO), getResources().getColor(R.color.tileColorP),
                getResources().getColor(R.color.tileColorR), getResources().getColor(R.color.tileColorQ));
        ;

        Collections.shuffle(colors);
        int cnt = 0;
        for (int i = 0; i < 16; i++) {

            cards.add(new Card(tmpX, tmpY, tmpWidth, tmpHeight, colors.get(cnt)));
            cnt++;
            if (cnt == 8) {
                cnt = 0;
                Collections.shuffle(colors);
            }

            tmpX += tmpWidth + tmpWidth / 10;
            if (((i + 1) % 4) == 0) {
                tmpX = tmpWidth / 3;
                tmpY += tmpWidth + tmpWidth / 10;
            }

        }
        tmpWidth = displayWidth / 5;
        tmpHeight = displayWidth / 5;
        tmpX = tmpWidth / 3;
        tmpY = tmpWidth / 3;
    }


    public void newGame() {
        cards.clear();
        openedCards = 0;
        isOnPauseNow = false;
        setColorsAndTiles();
        invalidate();
    }

}