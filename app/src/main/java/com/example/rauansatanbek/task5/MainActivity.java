package com.example.rauansatanbek.task5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DeviceDimensionsHelper.context = this;
        setContentView(new DrawView(this));

    }

    class DrawView extends View {

        Paint p;
        Path path;
        Matrix matrix;
        int mpx = 15;

        public DrawView(Context context) {
            super(context);
            p = new Paint();
            p.setStrokeWidth(3);
            p.setStyle(Paint.Style.STROKE);

            path = new Path();
            matrix = new Matrix();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawARGB(80, 102, 204, 255);
            path.reset();

            // угол
            path.moveTo(cpx(10), cpx(2));
            path.lineTo(cpx(10), cpx(12));
            path.lineTo(cpx(2), cpx(18));

            // треугольник
            path.moveTo(10, 12);
            path.lineTo(20, 12);
            path.close();

            p.setColor(Color.GRAY);
            canvas.drawPath(path, p);

            // квадрат и круг
            path.addCircle(450, 150, 25, Path.Direction.CW);

            // рисование path
            p.setColor(Color.GRAY);
            canvas.drawPath(path, p);

        }

        float cpx(float dp){
            return (float)DeviceDimensionsHelper.convertDpToPixel(dp*mpx);
        }
    }
}
