package com.wangjessica.jwlab10a;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable{

    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private Path mPath;

    private int mBitmapX;
    private int mBitmapY;
    private RectF mWinnerRect;

    private int mViewWidth;
    private int mViewHeight;
    private Bitmap mBitmap;

    private boolean mRunning;
    private Thread mGameThread;

    FlashlightCone mFlashlightCone;

    // Constructors
    public GameView(Context context) {
        super(context);
        init(context);
    }
    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }
    public GameView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHeight = h;

        mFlashlightCone = new FlashlightCone(mViewWidth, mViewHeight);

        // Set the font size
        mPaint.setTextSize(mViewHeight/5);

        // Create a bitmap
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.android);
        setUpBitmap();
    }

    @Override
    public void run() {
        Canvas canvas;
        while(mRunning){
            // Check if there is a valid Surface to draw on
            if(mSurfaceHolder.getSurface().isValid()){

                int x = mFlashlightCone.getmX();
                int y = mFlashlightCone.getmY();
                int radius = mFlashlightCone.getmRadius();

                // Lock the canvas
                canvas = mSurfaceHolder.lockCanvas();
                canvas.save();

                // Drawing on the canvas
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(mBitmap, mBitmapX, mBitmapY, mPaint);
                mPath.addCircle(x, y, radius, Path.Direction.CCW);
                canvas.clipPath(mPath, Region.Op.DIFFERENCE);
                canvas.drawColor(Color.BLACK); // Everything outside the circle will be black

                // Check if the flashlight is centered in the winning rectangle
                if(x > mWinnerRect.left && x < mWinnerRect.right && y > mWinnerRect.top && y < mWinnerRect.bottom){
                    // The user wins
                    canvas.drawColor(Color.WHITE);
                    canvas.drawBitmap(mBitmap, mBitmapX, mBitmapY, mPaint);
                    canvas.drawText("WIN!", mViewWidth/3, mViewHeight/2, mPaint);
                }

                // Drawing finished
                mPath.rewind();
                canvas.restore();
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setUpBitmap();
                updateFrame((int)x, (int)y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                updateFrame((int) x, (int) y);
                invalidate();
                break;
            default:
        }

        return true;
    }

    private void updateFrame(int newX, int newY){
        mFlashlightCone.update(newX, newY);
    }

    public void pause(){
        mRunning = false;
        try{
            // Stop the thread
            mGameThread.join();
        } catch(InterruptedException e){

        }
    }
    public void resume(){
        mRunning = true;
        mGameThread = new Thread(GameView.this);
        mGameThread.start();
    }
    private void init(Context context){
        mContext = context;
        mSurfaceHolder = getHolder();
        mPaint = new Paint();
        mPaint.setColor(Color.DKGRAY);
        mPath = new Path();
    }
    private void setUpBitmap(){
        // Find a random position
        mBitmapX = (int) Math.floor(Math.random()*(mViewWidth - mBitmap.getWidth()));
        mBitmapY = (int) Math.floor(Math.random()*(mViewHeight - mBitmap.getHeight()));
        mWinnerRect = new RectF(mBitmapX, mBitmapY, mBitmapX + mBitmap.getWidth(), mBitmapY + mBitmap.getHeight());
    }
}
