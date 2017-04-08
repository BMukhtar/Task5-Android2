package com.example.rauansatanbek.task5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    String LOG_TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceDimensionsHelper.context = this;
        setContentView(new DrawView(this));

    }

    class DrawView extends SurfaceView implements SurfaceHolder.Callback{

        private DrawThread drawThread;
        Paint p;
        Path path;
        Matrix matrix;
        int mpx = 15;
        String text;

        public DrawView(Context context) {
            super(context);
            getHolder().addCallback(this);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(LOG_TAG,"surfaceCreated");
            drawThread = new DrawThread(holder);
            drawThread.setRunning(true);
            drawThread.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(LOG_TAG,"surfaceChanged");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(LOG_TAG,"surfaceDestroyed");
            boolean retry = true;
            drawThread.setRunning(false);
            while (retry) {
                try {
                    drawThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }

        class DrawThread extends Thread {

            private boolean running = false;
            private SurfaceHolder surfaceHolder;

            public DrawThread(SurfaceHolder surfaceHolder) {
                this.surfaceHolder = surfaceHolder;
                p = new Paint();
                p.setStrokeWidth(2);
                p.setStyle(Paint.Style.STROKE);
                path = new Path();
                matrix = new Matrix();
                text = "Show shadow of cube by from view point";
            }

            @Override
            public void run() {
                Canvas canvas;
                int time = 10;
                int numberOfCycles = 30;
                float sleepTime = (float) time/numberOfCycles;
                int counter = 0;
                while (counter++<numberOfCycles) {
                    canvas = null;
                    try {
                        canvas = surfaceHolder.lockCanvas(null);
                        if (canvas == null)
                            continue;
                        matrix.reset();
                        matrix.setTranslate(-cpx(4f/numberOfCycles*counter), cpx(3f/numberOfCycles*counter));
                        canvas.drawRGB(102, 204, 255);
                        path.reset();

                        // axis
                        path.moveTo(cpx(10), cpx(2));
                        path.lineTo(cpx(10), cpx(12));
                        path.lineTo(cpx(2), cpx(18));
                        path.moveTo(cpx(10), cpx(12));
                        path.lineTo(cpx(20), cpx(12));

                        p.setColor(Color.GRAY);
                        canvas.drawPath(path, p);

                        //cube
                        path.reset();
                        path.addRect(new RectF(cpx(6),cpx(10),cpx(11),cpx(15)), Path.Direction.CW);
                        path.moveTo(cpx(6),cpx(10));
                        path.rLineTo(cpx(4),cpx(-3));
                        path.rLineTo(cpx(5),cpx(0));
                        path.rLineTo(cpx(0),cpx(5));
                        path.rLineTo(cpx(-4),cpx(3));
                        path.rMoveTo(cpx(0),cpx(-5));
                        path.rLineTo(cpx(4),cpx(-3));
                        p.setColor(Color.parseColor("#FF82B64C"));
                        canvas.drawPath(path, p);

                        //point path
                        path.reset();
                        path.moveTo(cpx(10),cpx(2));
                        path.rLineTo(cpx(-4),cpx(3));
                        p.setColor(Color.YELLOW);
                        canvas.drawPath(path,p);

                        //initialViews
                        path.reset();
                        path.addCircle(cpx(10),cpx(2),2, Path.Direction.CW);

                        Thread.sleep((long)(sleepTime*1000));
                        path.transform(matrix);
                        p.setColor(Color.BLUE);
                        canvas.drawPath(path, p);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (canvas != null) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                            Log.d(LOG_TAG,"unclock canvas called");
                        }
                    }
                }
            }

            public void setRunning(boolean running) {
                this.running = running;
            }
            float cpx(float dp){
                return (float)DeviceDimensionsHelper.convertDpToPixel(dp*mpx);
            }
        }
    }
}
