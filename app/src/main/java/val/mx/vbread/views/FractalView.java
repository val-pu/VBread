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
import android.view.ViewGroup;

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
    private BigDecimal verhaeltnis;

    public FractalView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        if (changed) {

            return;
        }

        bitmap = Bitmap.createBitmap(
                getMeasuredWidth(),
                getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bitmap);


    }

    public void init() {

        setBackgroundDrawable(new BitmapDrawable(bitmap));

        dimension = adapter.getSize();

        verhaeltnis = new BigDecimal(getMeasuredWidth()).divide(new BigDecimal(getMeasuredWidth()), 3, RoundingMode.DOWN);
        Adapter.verhaeltnis = verhaeltnis;

        Log.i("Unnoetiges Verhältnis", "Nutze das Verhältnis 1 : " + verhaeltnis);

        Log.i("Zeichnen", "Zeichenprozess hat begonnen");

        DrawTask task = new DrawTask(this);
        task.execute(1);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x1 = 0, x2, y1 = 0, y2, dx, dy;
        String direction;
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                x1 = event.getX();
                y1 = event.getY();
                break;

            case (MotionEvent.ACTION_UP): {
                x2 = event.getX();
                y2 = event.getY();
                dx = x2 - x1;
                dy = y2 - y1;

                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0)
                        direction = "right";
                    else
                        direction = "left";
                } else {
                    if (dy > 0)
                        direction = "down";
                    else
                        direction = "up";
                }
            }
        }

        return false;
    }

    /**
     * Berechnung im Hintergrund, um Rechenleistung zu verteilen
     */
    private class DrawTask extends AsyncTask<Integer, Void, LinkedList<DrawInfo>> {
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

        @Override
        protected LinkedList<DrawInfo> doInBackground(Integer... size) {
            Log.i("Zeichnen", "Frage Farben ab und setze Pixel");

            Canvas canvas = fractalView.getCanvas();

            // Label um diese Schleife ggf. zu zerstören
            root:
            for (int i = 7; 0 <= i; i--) {


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
                        RectF rectF = new RectF(inf.getScreenX(), inf.getScreenY(), inf.getScreenX() + width, inf.getScreenY() + width);
                        p.setColor(inf.getColor());
                        canvas.drawRect(rectF, p);

                    }

                    // Zeichnet den Bildschirm neu
                    invalidate();
                }
            }


            // TODO: 30.10.2020 ADD RESOLUTION EXTRA


            return null;
        }

        @Override
        protected void onPostExecute(LinkedList<DrawInfo> infos) {
            Log.i("Zeichnen", "Zeichnen ist fertig");
        }
    }

    public abstract static class Adapter {

        protected static BigDecimal verhaeltnis;
        protected Dimension dimension;
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
