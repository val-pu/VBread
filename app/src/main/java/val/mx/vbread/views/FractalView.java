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

import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.ui.home.HomeFragment;

/**
 * @author Lukas Hensel
 * @since 10 Nov. 15: 08 Geschichte PKT
 */

public class FractalView extends androidx.appcompat.widget.AppCompatImageView implements View.OnTouchListener {
    // Wunderschoene Variablen

    private int lastTask = 0;
    private Adapter adapter = null;
    private Bitmap bitmap;
    private Canvas canvas;
    private Dimension dimension;
    public HomeFragment homeFragment;
    private BigDecimal ratio;

    public FractalView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }



    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);

        getRootView().post(() -> {
            bitmap = Bitmap.createBitmap(
                    getMeasuredWidth(),
                    getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);

            canvas = new Canvas(bitmap);
            setBackgroundDrawable(new BitmapDrawable(bitmap));
        });
    }



    public void init() {



        dimension = adapter.getSize();
        setOnTouchListener(this);

        ratio = new BigDecimal(getMeasuredWidth()).divide(new BigDecimal(getMeasuredHeight()), 3, RoundingMode.DOWN);
        Adapter.verhaeltnis = ratio;

        Log.i("Unnoetiges Verhältnis", "Nutze das Verhältnis 1 : " + ratio);

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

        // Bewgen der

        if(adapter == null) return false;

        float x2, y2, dx, dy;
        String direction;

        switch (event.getAction()) {

            case 261:
                Log.i("ZOOMER","ZOOMING");
                return true;

            case (MotionEvent.ACTION_DOWN):
                Log.i("Swipe direction","testDOWn");
                x1 = event.getX();
                y1 = event.getY();
                return true;


            case (MotionEvent.ACTION_MOVE): {

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

                touchCount++;
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
                HomeFragment.Companion.onResult(dimension.getLeft(), dimension.getTop(),diameter, adapter.itera);
                if(touchCount%4 == 0) {
                    touchCount = 0;

                }

            }



        }
        y1 = event.getY();
        x1 = event.getX();
        homeFragment.updateUI();

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
            taskId = lastTask +2;
            lastTask = taskId;

            Log.i("TASK","TASK ID " + taskId + " started!");
        }

        @Override
        protected Void doInBackground(Void... size) {
//            Log.i("Zeichnen", "Frage Farben ab und setze Pixel");


            Dimension tempDimension = dimension;

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

                    // Frage die Userdefinierten Aktionen bei einer neuen Linie ab
                    adapter.onNewLine();

                    // Falls ein neuer Zeichenprozess erstellt wird wird dieser Unterbrochen


                    screenY = j * width;

                    // Falls die maximale anzahl horizontaler Pixel ueberschritten wird, so
                    if (screenY > getWidth()) break;

                    // Versuch der optimisation TODO erklaeren
                    if( (screenY)%(width*2) == 0) continue;

                    BigDecimal y = getPoint(tempDimension.getDown(), tempDimension.getTop(), count, j);

                    for (int k = 0; true; k++) {

                        screenX = k * width;

                        if( (screenX)%(width*2) == 0) {
                            continue;
                        }

                        if (screenX > getWidth()) break;

                        // Initialisiere Berechnungsgrundlage
                        DrawInfo inf = new DrawInfo(getPoint(tempDimension.getLeft(), tempDimension.getRight(), count, k), y, k * (width), j * (int) width);

                        // Frage Farben ab { @see FractalView.Adapter#onDraw(info : DrawInfo) }
                        inf = fractalView.getAdapter().onDraw(inf);

                        if(inf.getColor() == -100)
                            try {
                                throw new Exception("Fehler: Farbe wurde NICHT definiert !");
                            } catch (Exception e) {
                                inf.setColor(1);
                            }


                        // Setze Pixel
                        RectF rectF = new RectF(inf.getScreenX(), inf.getScreenY(), inf.getScreenX() + width*2, inf.getScreenY() + width*2);
                        p.setColor(inf.getColor());
                        canvas.drawRect(rectF, p);

                    }
                    // Falls ein neuer Task gestartet wurde, so wird dieser abgebrochen
                    if (taskId != lastTask) return null;
                    invalidate();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void infos) {
            Log.i("Zeichnen", "Task ID " + taskId + " is done");
        }
    }


    // Klasse, um seine eigenen Fraktale einzubinden
    public abstract static class Adapter {

        public void onNewLine() {}

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
