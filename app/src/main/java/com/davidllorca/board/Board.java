package com.davidllorca.board;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class Board extends View {

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private float mX, mY;
    private static final float TOLERANCE = 4;
    private Paint mPaint;

    // Contructors

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Board(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        // Get display parameters
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        // Gets the size of the display, in pixels. Point is object to receive the size information.
        display.getSize(point);
        mBitmap = Bitmap.createBitmap(point.x, point.y, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPath = new Path();
        // Prepare Paint
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0XFF00E1FF);
        mPaint.setStrokeWidth(10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            // Start touch screen event
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                invalidate();
                break;
            // Move over screen event
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            // Stop touch screen event
            case MotionEvent.ACTION_UP:
                touchUp(x, y);
                invalidate();
                break;
        }
        return true;
    }

    /**
     * Start touch screen. Init Path.
     *
     * @param x
     * @param y
     */
    private void touchStart(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    /**
     * Moving over screen.
     *
     * @param x
     * @param y
     */
    private void touchMove(float x, float y) {
        // If CPU is faster TOLERANCE can be less.
        // For better visual result, paint do a curve, not a straight line
        if (Math.abs(x - mX) >= TOLERANCE || Math.abs(y - mY) >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    /**
     * Stop touch screen. Finish stroke. Save Bitmap.
     *
     * @param x
     * @param y
     */
    private void touchUp(float x, float y) {
        mPath.lineTo(x, y);
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Background
        canvas.drawColor(0XFFBBBBBB);
        // Strokes
        canvas.drawBitmap(mBitmap, 0, 0, null);
        // Last stroke
        canvas.drawPath(mPath, mPaint);
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void clear(Context context) {
        // Get display parameters
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point point = new Point();
        // Gets the size of the display, in pixels. Point is object to receive the size information.
        display.getSize(point);
        mBitmap = Bitmap.createBitmap(point.x, point.y, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        invalidate();
    }

    public void setEraser() {
        // Set stroke mode clear
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        /*
            Other modes
            PorterDuff.Mode.LIGHTEN (lighter)
            PorterDuff.Mode.XOR...
         */
    }

    public void setPaintColor(int color) {
        mPaint.setColor(color);
        mPaint.setXfermode(null);
    }
}



