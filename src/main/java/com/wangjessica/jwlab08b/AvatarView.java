package com.wangjessica.jwlab08b;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AvatarView extends View {
    Map<String, String> colors = new HashMap<String, String>();
    int W;
    int H;
    Paint p = new Paint();
//    String head;
//    String body;
//    String foot1;
//    String foot2;
    public AvatarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        colors.put("head", "white");
        colors.put("body", "white");
        colors.put("foot1", "white");
        colors.put("foot2", "white");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        W = getWidth();
        H = getHeight();
        // Head
        changePaint("head");
        canvas.drawCircle(W*0.5f, H*0.19f, H*0.09f, p);
        // Body
        changePaint("body");
        canvas.drawOval(W*0.15f, H*0.32f, W*0.85f, H*0.74f, p);
        // Foot 1
        changePaint("foot1");
        canvas.drawCircle(W*0.32f, H*0.83f, W*0.08f, p);
        // Foot 2
        changePaint("foot2");
        canvas.drawCircle(W*0.68f, H*0.83f, W*0.08f, p);
        // Update
        invalidate();
    }
    public void changePaint(String part){
        p.setColor(getResources().getColor(getResources().getIdentifier(colors.get(part), "color", "com.wangjessica.jwlab08b"), null));
    }
    public void changeColor(String part, String color){
        colors.replace(part, color);
    }
}
