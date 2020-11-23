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

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import val.mx.vbread.adapters.FractalPickerAdapter;
import val.mx.vbread.adapters.MandelBrotAdapter;
import val.mx.vbread.containers.ColorPalette;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.ui.home.HomeFragment;

/**
 * @author Lukas Hensel
 * @since 10 Nov. 15: 08 Geschichte PKT
 */

public class FractalView extends androidx.appcompat.widget.AppCompatImageView implements View.OnTouchListener, FractalPickerAdapter.FractalPickListener {
    // Wunderschoene Variablen

    private double zoomFactor = .90d;
    private MotionEvent lastEvent = null;

    private int lastTask = 0;
    private int lastZoomDistance = 0;
    public static Adapter adapter = new MandelBrotAdapter();
    private Bitmap bitmap;
    private Canvas canvas;
    public HomeFragment homeFragment;
    private BigDecimal ratio;
    private ColorPalette palette;

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
//            init();
        });
    }


    public void init() {

        if (adapter.getDimension() == null) adapter.setDimension(adapter.getInitialSize());


        /*ratio = new BigDecimal(getMeasuredWidth()).divide(new BigDecimal(getMeasuredHeight()), 3, RoundingMode.DOWN);
        Adapter.verhaeltnis = ratio;*/

        Log.i("Zeichnen", "Zeichenprozess hat begonnen Dimension ist " + adapter.dimension);

        DrawTask task = new DrawTask(this);

        task.execute();
    }

    public void setAdapter(Adapter adapter) {
        assert getWidth() != 0;
        this.adapter = adapter;
        init();
    }

    private BigDecimal step, low;

    public void initGetPoints(BigDecimal low, BigDecimal high, int count) {
        this.low = low;
        BigDecimal combinedRange = high.subtract(low).abs();
        step = combinedRange.divide(new BigDecimal(count), 40, RoundingMode.DOWN);
    }

    public double getPoint(int index) {
        assert step != null;
        return step.multiply(new BigDecimal(index)).add(low).doubleValue();
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

    private int getDifference(MotionEvent e) {

        int dx = (int) (e.getX(0) - e.getX(1));
        int dy = (int) (e.getY(0) - e.getY(1));

        return (int) Math.sqrt(Math.pow(dy, 2) * Math.pow(dx, 2));
    }

    private BigDecimal getMiddle(BigDecimal b1, BigDecimal b2) {
        return b2.add(b1).divide(new BigDecimal(2), 20, RoundingMode.DOWN);
    }

    boolean lastAction = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (adapter == null) return false;

        float x2;
        float y2;
        float dx;
        float dy;


        if (event.getPointerCount() == 2) {
            touchCount++;

            if (touchCount % 5 != 0) return true;

            BigDecimal left = adapter.dimension.getLeft();
            BigDecimal right = adapter.dimension.getRight();
            BigDecimal diameter = right.abs().subtract(left.abs()).abs();

            int currentDiff = getDifference(event);

            int action = event.getAction();
            int mainAction = action & MotionEvent.ACTION_MASK;

            if (mainAction == MotionEvent.ACTION_POINTER_DOWN) {
                lastZoomDistance = getDifference(event);
                Log.i("Zoom", "Got first zoom event");
            } else if (mainAction == MotionEvent.ACTION_POINTER_UP) {
                lastZoomDistance = -1;
            } else if (lastZoomDistance != -1) {
                BigDecimal old = diameter;


                double customZoomFac = zoomFactor;

                if (lastZoomDistance >= currentDiff) {

                    Log.i("Zoom", "Zooming Up");

                    diameter = diameter.divide(new BigDecimal(customZoomFac), 12, RoundingMode.DOWN).abs();
                    BigDecimal dd = old.subtract(diameter).multiply(new BigDecimal("0.5")).abs();
                    adapter.dimension = new Dimension(adapter.dimension.getLeft().subtract(dd), adapter.dimension.getRight().add(dd), adapter.dimension.getTop().add(dd), adapter.dimension.getDown().subtract(dd));

                } else {

                    diameter = diameter/*.divide(new BigDecimal(2),12,BigDecimal.ROUND_DOWN)*/.multiply(new BigDecimal(customZoomFac), MathContext.DECIMAL128).abs();
                    BigDecimal dd = old.subtract(diameter).multiply(new BigDecimal("0.5")).abs();

                    adapter.dimension = new Dimension(adapter.dimension.getLeft().add(dd), adapter.dimension.getRight().subtract(dd), adapter.dimension.getTop().subtract(dd), adapter.dimension.getDown().add(dd));
                }
                HomeFragment.Companion.onResult(adapter.dimension.getLeft(), adapter.dimension.getTop(), diameter, adapter.itera);
                homeFragment.updateUI();
                lastZoomDistance = getDifference(event);

            }


            lastZoomDistance = currentDiff;


            return false;
        } else {

            if (event.getPointerCount() == 2) return false;
            switch (event.getAction()) {
                case (MotionEvent.ACTION_DOWN):

                    lastEvent = null;
                    return true;


                case (MotionEvent.ACTION_MOVE): {

                    lastZoomDistance = -1;

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
//                System.out.println("DIAMETER" + diameter);
                    BigDecimal step = diameter.divide(new BigDecimal(100), 20, RoundingMode.DOWN);
                    BigDecimal stepX, stepY;
                    stepX = stepY = step;
                    // Hinzufügen eines schnelleren swipe effektes
                    if (lastEvent != null) {
                        double difference = getDifference(dx, dy);
                        System.out.println("Diffrence " + difference);
                        stepX = step.multiply(BigDecimal.valueOf(1 + ( dx) / 100D));
                        stepY = step.multiply(BigDecimal.valueOf(1 + ( dy) / 100D));
                    }

                    touchCount++;

                        if (dx > 0) {
                            left = left.subtract(stepX);
                            right = right.subtract(stepX);
                        } else {
                            left = left.add(stepX);
                            right = right.add(stepX);
                        }


                    if (dy > 0) {
                        top = top.subtract(stepY);
                        down = down.subtract(stepY);
                    } else {
                        top = top.add(stepY);
                        down = down.add(stepY);
                    }

                    dimension = new Dimension(left, right, top, down);
//                adapter.dimension = dimension;
                    HomeFragment.Companion.onResult(dimension.getLeft(), dimension.getTop(), diameter, adapter.itera);
                    if (touchCount % 4 == 0) {
                        touchCount = 0;

                    }
                    x1 = event.getX();
                    y1 = event.getY();

                }


            }
        }
        y1 = event.getY();
        x1 = event.getX();
        lastEvent = event;
        homeFragment.updateUI();
        return false;
    }

    private double getDifference(float dx, float dy) {
        return  Math.sqrt(Math.pow(dx, 2) * Math.pow(dy, 2));
    }

    public void setColorPalette(ColorPalette palette) {
        this.palette = palette;
    }

    // Falls ein neues Fraktal ausgewählt wird, so wird dieses hier registriert
    @Override
    public void onFractalPick(@NotNull Adapter adapter) {

        adapter.setDimension(adapter.getInitialSize());
        Dimension dimension = adapter.getDimension();
        BigDecimal diameter = dimension.getLeft().subtract(dimension.getRight()).abs();

        FractalView.adapter = adapter;

        HomeFragment.Companion.onResult(adapter.dimension.getLeft(), adapter.dimension.getTop(), diameter, adapter.itera);
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
            taskId = lastTask + 2;
            lastTask = taskId;

            Log.i("TASK", "TASK ID " + taskId + " started!");
        }

        @Override
        protected Void doInBackground(Void... size) {
//            Log.i("Zeichnen", "Frage Farben ab und setze Pixel");


            Dimension tempDimension = adapter.dimension;

            Canvas canvas = fractalView.getCanvas();


            // Label um diese Schleife ggf. zu zerstören
            for (int i = 5; 0 <= i; i--) {


                int width = (int) Math.pow(2, i);
                if (i == 0) width = 1;

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
                    if ((screenY) % (width * 2) == 0) continue;

                    initGetPoints(tempDimension.getDown(), tempDimension.getTop(), count);

                    double y = getPoint(j);

                    initGetPoints(tempDimension.getLeft(), tempDimension.getRight(), count);
                    for (int k = 0; true; k++) {

                        screenX = k * width;

                        if ((screenX) % (width * 2) == 0) {
                            continue;
                        }

                        if (screenX > getWidth()) break;

                        // Initialisiere Berechnungsgrundlage
                        DrawInfo inf = new DrawInfo(getPoint(/*tempDimension.getLeft(), tempDimension.getRight(), count,*/ k), y, k * (width), j * (int) width);

                        // Frage Farben ab { @see FractalView.Adapter#onDraw(info : DrawInfo) }
                        inf = fractalView.getAdapter().onDraw(inf);

                        if (inf.getColor() == -100)
                            try {
                                throw new Exception("Fehler: Farbe wurde NICHT definiert !");
                            } catch (Exception e) {
                                inf.setColor(1);
                            }


                        // Setze Pixel
                        RectF rectF = new RectF(inf.getScreenX(), inf.getScreenY(), inf.getScreenX() + width * 2, inf.getScreenY() + width * 2);
                        p.setColor(inf.getColor());
                        canvas.drawRect(rectF, p);

                    }
                    // Falls ein neuer Task gestartet wurde, so wird dieser abgebrochen
                    if (taskId != lastTask) return null;
                }
                invalidate();
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

        public void onNewLine() {
        }


        protected Dimension dimension;
        public int itera = 100;

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
        public abstract Dimension getInitialSize();

        public Dimension getDimension() {
            return dimension;
        }

        public boolean usesIntegratedColorPalette() {
            return true;
        }

    }

}
