package com.wangjessica.jwlab08b;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GameView extends View {
    Map<String, String> avatar;
    RectF basket;
    Bitmap basketPic;
    String season;
    Bitmap picture;
    int score = 0;
    int objCnt = 6;
    int delay = 800;
    boolean gainedPoints = true;
    Point[] objs;
    boolean drawObjects = true;
    int W = 0;
    int H = 0;
    float y = 0.75f;
    float x = 0.15f; // Horizontal position
    boolean handlerFinished = true;
    boolean hardMode = false;
    Paint p = new Paint();
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        objs = new Point[objCnt];
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);
        picture = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(season+"obj", "drawable", "com.wangjessica.jwlab08b"), null);
        basketPic = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(season+"basket", "drawable", "com.wangjessica.jwlab08b"), null);
        for(int i=0; i<objs.length; i++){
            objs[i] = new Point((float)Math.random(), (float)Math.random()*0.5f);
        }
        System.out.println("Setting map..");
        if(avatar==null) avatar = new HashMap<String, String>();
        System.out.println(avatar);
        H = getHeight();
        W = getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawAvatar(canvas);
        drawObjects(canvas);
        if(handlerFinished&&!hardMode){
            handlerFinished = false;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawObjects = true;
                    System.out.println("Calling invalidate");
                    handlerFinished = true;
                    invalidate();
                }
            }, delay);
        }
    }
    public void drawAvatar(Canvas canvas){
        // Head
        changePaint("head");
        canvas.drawCircle(W*x, H*(y+0.065f), 35, p);
        // Body
        changePaint("body");
        canvas.drawOval(W*(x-0.04f), H*(y+0.1f), W*(x+0.04f), H*(y+0.19f), p);
        // Left Foot
        changePaint("foot1");
        canvas.drawCircle(W*(x-0.04f), H*(y+0.21f), 10, p);
        // Right Foot
        changePaint("foot2");
        canvas.drawCircle(W*(x+0.04f), H*(y+0.21f), 10, p);
        // Basket
        basket = new RectF(W*(x+0.03f), H*(y+0.1f), W*(x+0.1f), H*(y+0.14f));
       // canvas.drawRect(basket, p);
        //p.setColor(Color.RED);
        //canvas.drawRect(basket, p);
        canvas.drawBitmap(basketPic, null, basket, null);
        for(int i=0; i<objs.length; i++){
            Point p = objs[i];
            RectF rect = new RectF(W*(p.x-0.05f), H*(p.x+0.05f), W*(p.x+0.05f), H*(p.y+0.05f));
            if(basket.intersect(rect)){
                // Get new object!!
                objs[i] = new Point((float)Math.random(), (float)Math.random()*0.5f);
                score++;//System.out.println("Got something, score = "+ ++score);
                gainedPoints = true;
                invalidate();
            }
        }
    }
    public void drawObjects(Canvas canvas){
        System.out.println("Drawing objects..");
        System.out.println(drawObjects);
        for(Point p: objs){
            if(drawObjects){
                p.y+=0.1;
                if(p.y>1){
                    p.y = (float)Math.random()*0.5f;
                }
            }
            RectF rect = new RectF(W*(p.x-0.05f), H*(p.y-0.05f), W*(p.x+0.05f), H*(p.y+0.05f));
            canvas.drawBitmap(picture, null, rect, null);
        }
        drawObjects = false;
    }

    public void changePaint(String part){
//        System.out.println("Avatar..");
//        System.out.println(avatar);
        p.setColor(getResources().getColor(getResources().getIdentifier(avatar.getOrDefault(part, "white"), "color", "com.wangjessica.jwlab08b"), null));
    }
    class Point{
        float x;
        float y;
        public Point(float a, float b){
            x = a;
            y = b;
        }
    }
}
