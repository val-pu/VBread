package val.mx.vbread.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.ui.home.HomeFragment;

public class FractalView extends androidx.appcompat.widget.AppCompatImageView implements View.OnTouchListener {
    private int lastTask = 0;
    private Adapter adapter = null;
    private Bitmap bitmap;
    private int resolution = 2, resolutionExtra;
    private Canvas canvas;
    private Dimension dimension;
    public HomeFragment homeFragment;
    private BigDecimal verhaeltnis;

    public FractalView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }



    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);

        getRootView().post(new Runnable() {
            @Override
            public void run() {
                bitmap = Bitmap.createBitmap(
                        getMeasuredWidth(),
                        getMeasuredHeight(),
                        Bitmap.Config.ARGB_8888);

                canvas = new Canvas(bitmap);
            }
        });
    }



    public void init() {

        setBackgroundDrawable(new BitmapDrawable(bitmap));

        dimension = adapter.getSize();
        setOnTouchListener(this);

        verhaeltnis = new BigDecimal(getMeasuredWidth()).divide(new BigDecimal(getMeasuredWidth()), 3, RoundingMode.DOWN);
        Adapter.verhaeltnis = verhaeltnis;

        Log.i("Unnoetiges Verhältnis", "Nutze das Verhältnis 1 : " + verhaeltnis);

        Log.i("Zeichnen", "Zeichenprozess hat begonnen");

        DrawTask task = new DrawTask(this);
        task.execute();
    }

    public void setAdapter(Adapter adapter) {
        assert getWidth() != 0;
        this.adapter = adapter;
        init();
    }

    public static BigDecimal getPoint(BigDecimal low, BigDecimal high, int count, int index) {
        BigDecimal combinedRange = high.subtract(low).abs();
        BigDecimal step = combinedRange.divide(new BigDecimal(count), 40, RoundingMode.DOWN);
        return step.multiply(new BigDecimal(index)).add(low);
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Canvas getCanvas() {
        return canvas;
    }
    float x1 = 0, y1 = 0;

    private int touchCount = 0;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(adapter == null) return false;

        float x2, y2, dx, dy;
        String direction;
        Log.i("Swipe direction",event.getAction() + "");
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                Log.i("Swipe direction","testDOWn");
                x1 = event.getX();
                y1 = event.getY();
                return true;


            case (MotionEvent.ACTION_MOVE): {
                touchCount++;

                Log.i("Swipe direction","testUP");
                x2 = event.getX();
                y2 = event.getY();
                dx = x2 - x1;
                dy = y2 - y1;
                Dimension dimension = adapter.dimension;

                BigDecimal down = dimension.getDown();
                BigDecimal top = dimension.getTop();
                BigDecimal left = dimension.getLeft();
                BigDecimal right = dimension.getRight();
                BigDecimal diameter = left.subtract(right).abs();
                BigDecimal step = left.subtract(right).divide(new BigDecimal(100),2000,RoundingMode.DOWN);


                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0) {
                        left = left.subtract(step);
                        right = right.subtract(step);
                    } else {
                        left = left.add(step);
                        right = right.add(step);
                    }

                }
                    if (dy > 0) {
                        top = top.subtract(step);
                        down = down.subtract(step);
                    } else {
                        top = top.add(step);
                        down = down.add(step);
                    }



                dimension = new Dimension(left,right,top,down);
                adapter.dimension = dimension;

                if(touchCount%4 == 0) {
                    touchCount = 0;
                    setAdapter(adapter);
                }

            }
            homeFragment.updateUI();
            x1 = event.getX();
            y1 = event.getY();
        }

        return false;
    }

    /**
     * Berechnung im Hintergrund, um Rechenleistung zu verteilen
     */
    private class DrawTask extends AsyncTask<Void, Void, Void> {
        private Paint p = new Paint();
        private FractalView fractalView;
        private int taskId;

        public DrawTask(FractalView view) {
            this.fractalView = view;
        }

        @Override
        protected void onPreExecute() {
            // Zuweisung einer einzigartigen ID
            taskId = ++lastTask;
        }

        private Float dicke = 0.3f;

        @Override
        protected Void doInBackground(Void... size) {
//            Log.i("Zeichnen", "Frage Farben ab und setze Pixel");


            Canvas canvas = fractalView.getCanvas();

            // Label um diese Schleife ggf. zu zerstören
            root:
            for (int i = 5; 0 <= i; i--) {


                int width = (int) Math.pow(2, i);
                if(i == 0) width = 1;

                int count;

                if (getWidth() % width == 0) count = getWidth() / width;
                else count = getWidth() / width + 1;

                int screenX, screenY;

                for (int j = 0; true; j++) {

                    // Falls ein neuer Zeichenprozess erstellt wird wird dieser Unterbrochen
                    if (taskId != lastTask) break root;

                    screenY = j * width;

                    if (screenY > getWidth()) break;

                    BigDecimal y = getPoint(dimension.getDown(), dimension.getTop(), count, j);

                    for (int k = 0; true; k++) {
                        screenX = k * width;

                        if (screenX > getWidth()) break;

                        // Initialisiere Berechnungsgrundlage
                        DrawInfo inf = new DrawInfo(getPoint(dimension.getLeft(), dimension.getRight(), count, k), y, k * (int) width, j * (int) width);


                        // Frage Farben ab { @see FractalView.Adapter#onDraw(info : DrawInfo) }

                        inf = fractalView.getAdapter().onDraw(inf);

                        // Setze Pixel
                        RectF rectF = new RectF(inf.getScreenX() + dicke/2, inf.getScreenY() +dicke/2, inf.getScreenX() + width + dicke/2, inf.getScreenY() + width + dicke/2);
                        p.setColor(inf.getColor());
                        canvas.drawRect(rectF, p);

                    }

                    // Zeichnet den Bildschirm neu
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        invalidate();
                    }
                }).start();
            }


            // TODO: 30.10.2020 ADD RESOLUTION EXTRA


            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void infos) {
//            Log.i("Zeichnen", "Zeichnen ist fertig");
        }
    }

    public abstract static class Adapter {

        protected static BigDecimal verhaeltnis;
        Dimension dimension;
        public int itera;

        public void setDimension(Dimension dimension) {
            this.dimension = dimension;
        }

        public void setItera(int itera) {
            this.itera = itera;
        }

        /**
         * Wird immer aufgerufen, falls ein Pixel gezeichnet wird -> Logik hier
         *
         * @param info Info zum zu zeichnenden Pixel
         * @return Farbe des zu zeichnenden Pixels
         */
        public abstract DrawInfo onDraw(DrawInfo info);

        /**
         * @return die Größe des KoordinatenSystems
         */
        public abstract Dimension getSize();
    }

}
