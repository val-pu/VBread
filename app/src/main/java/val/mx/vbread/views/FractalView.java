package val.mx.vbread.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
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
import val.mx.vbread.data.Settings;
import val.mx.vbread.ui.home.HomeFragment;

/**
 * @author Lukas Hensel
 * @since 10 Nov. 15: 08 Geschichte PKT
 */

@SuppressWarnings({"deprecation", "JavaDoc"})
public class FractalView extends androidx.appcompat.widget.AppCompatImageView implements View.OnTouchListener, FractalPickerAdapter.FractalPickListener {
    // Wunderschoene Variablen

    private static ParameterRequestEvent parameterRequest = null;
    private int lastTask = 1;
    private int lastZoomDistance = 0;
    public static Adapter adapter = new MandelBrotAdapter();
    private Bitmap bitmap;
    private Canvas canvas;
    private Settings settings;
    public HomeFragment homeFragment;
    private ColorPalette palette;

    public FractalView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        settings = Settings.Companion.getInstance();

        // Setze erste Farbpalette
        palette = new ColorPalette("Standard", Color.WHITE,
                Color.rgb(9, 1, 47),
                Color.rgb(4, 4, 73),
                Color.rgb(0, 7, 100),
                Color.rgb(12, 44, 138),
                Color.rgb(24, 82, 177),
                Color.rgb(57, 125, 209),
                Color.rgb(134, 181, 229),
                Color.rgb(211, 236, 248),
                Color.rgb(241, 233, 191),
                Color.rgb(248, 201, 95),
                Color.rgb(255, 170, 0),
                Color.rgb(204, 128, 0),
                Color.rgb(153, 87, 0),
                Color.rgb(106, 52, 3)
                //SOURCE https://stackoverflow.com/questions/16500656/which-color-gradient-is-used-to-color-mandelbrot-in-wikipedia
        );

        setOnTouchListener(this);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        // Frage ab ob layout verändert wurde und setze Bild im Hintergrund welches die Menge enthalten wird

        super.onLayout(changed, left, top, right, bottom);

        Double scale = settings.getResolutionScale();

        System.out.println("SCALE" + scale);

        int width = (int) (getMeasuredWidth() * scale);
        int height = (int) (getMeasuredHeight() * scale);

        // die Klasse Canvas ist dazu geadacht BitMaps, also im endeffekt Bilder zu manipulieren

        if (canvas == null)
            // Ausführung der Befehle nachdem die App fertig gestartet ist
            getRootView().post(() -> {
                // Erstellung einer neuen Bitmap, da noch keine vorhanden ist
                try {


                    bitmap = Bitmap.createBitmap(
                            width,
                            height,
                            Bitmap.Config.ARGB_8888);

                    canvas = new Canvas(bitmap);
                    setBackgroundDrawable(new BitmapDrawable(bitmap));
                    adapter = new MandelBrotAdapter();
                    init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        else {

            // Erstellung einer neuen BitMap, falls sich die Höhe verändert hat
            if (bitmap.getHeight() == getHeight()) return;
            bitmap = Bitmap.createBitmap(
                    width,
                    height,
                    Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            setBackgroundDrawable(new BitmapDrawable(bitmap));

            // Zeichnung wird durch die Setzung des Adapters initialisiert
            setAdapter(adapter);
        }
    }


    /**
     * Methode initiert den Zeichenvorgang
     */
    public void init() {

        if (adapter.getDimension() == null) adapter.setDimension(adapter.getInitialSize());

        // Log
        Log.i("Zeichnen", "Zeichenprozess hat begonnen Dimension ist " + adapter.dimension);
        DrawTask task;
        task = new DrawTask(this);
        // Starte task
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * @param adapter neuer adapter
     * @see FractalView#init() wird automatisch aufgerufen
     */

    @SuppressLint("Assert")
    public void setAdapter(Adapter adapter) {
        assert getWidth() != 0;
        FractalView.adapter = adapter;
        init();
    }

    /**
     * Methode nimmt den Absoluten abstand zwischen { low } und { high } teilt diesen durch { count } und gibt den Wert zurück
     *
     * @param low
     * @param high
     * @param count
     * @return berechneter wert
     */

    public BigDecimal initGetPoints(BigDecimal low, BigDecimal high, int count) {
        BigDecimal combinedRange = high.subtract(low).abs();
        return combinedRange.divide(new BigDecimal(count), 10, RoundingMode.DOWN);
    }

    public double getPoint(int index, BigDecimal step, BigDecimal low) {
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

    // Handeln des Zoomens / bewegen des Fraktals (für das Fraktal irrelevant)

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
                lastZoomDistance = -1;
                // Log.i("Zoom", "Got first zoom event");
            } else if (mainAction == MotionEvent.ACTION_POINTER_UP) {
                lastZoomDistance = -1;
            } else if (lastZoomDistance != -1) {
                BigDecimal old = diameter;

                double zoomFactor = .90d;

                if (lastZoomDistance >= currentDiff) {
                    diameter = diameter.divide(new BigDecimal(zoomFactor), 10, RoundingMode.DOWN).abs();
                    BigDecimal dd = old.subtract(diameter).multiply(new BigDecimal("0.5")).abs();
                    adapter.dimension = new Dimension(adapter.dimension.getLeft().subtract(dd), adapter.dimension.getRight().add(dd), adapter.dimension.getTop().add(dd), adapter.dimension.getDown().subtract(dd));
                } else {
                    diameter = diameter.multiply(new BigDecimal(zoomFactor), MathContext.DECIMAL32).abs();
                    BigDecimal dd = old.subtract(diameter).multiply(new BigDecimal("0.5")).abs();
                    adapter.dimension = new Dimension(adapter.dimension.getLeft().add(dd), adapter.dimension.getRight().subtract(dd), adapter.dimension.getTop().subtract(dd), adapter.dimension.getDown().add(dd));
                }
                HomeFragment.Companion.onResult(adapter.dimension.getLeft(), adapter.dimension.getTop(), diameter, adapter.itera);
                homeFragment.updateUI();

            }


            lastZoomDistance = currentDiff;


            return false;
        } else {

            switch (event.getAction()) {
                case (MotionEvent.ACTION_DOWN):

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
                    BigDecimal stepX, stepY;

                    // Hinzufügen eines schnelleren swipe effektes
                    if (x1 != 0) {
                        stepX = diameter.multiply(BigDecimal.valueOf((dx / getMeasuredWidth()) * -1));
                        stepY = diameter.multiply(BigDecimal.valueOf((dy / getMeasuredHeight()) * -1));
                    } else {
                        x1 = event.getX();
                        y1 = event.getY();
                        return true;
                    }

                    touchCount++;

                    left = left.add(stepX);
                    right = right.add(stepX);

                    top = top.add(stepY);
                    down = down.add(stepY);

                    adapter.dimension = new Dimension(left, right, top, down);

                    if (touchCount % 4 == 0) {
                        touchCount = 0;
                        HomeFragment.Companion.onResult(adapter.dimension.getLeft(), adapter.dimension.getTop(), adapter.dimension.getDiameter(), adapter.itera);

                    }
                    x1 = event.getX();
                    y1 = event.getY();
                    break;
                }
                case MotionEvent.ACTION_UP:
                    x1 = 0;
                    y1 = 0;
                    HomeFragment.Companion.onResult(adapter.dimension.getLeft(), adapter.dimension.getTop(), adapter.dimension.getDiameter(), adapter.itera);
                    return false;

            }
        }
        y1 = event.getY();
        x1 = event.getX();
        homeFragment.updateUI();
        return true;
    }

    public void setColorPalette(ColorPalette palette) {
        this.palette = palette;
    }

    public ColorPalette getPalette() {
        return palette;
    }

    // Falls ein neues Fraktal ausgewählt wird, so wird dieses hier registriert
    @Override
    public void onFractalPick(@NotNull Adapter adapter) {

        adapter.setDimension(adapter.getInitialSize());
        Dimension dimension = adapter.getDimension();
        BigDecimal diameter = dimension.getLeft().subtract(dimension.getRight()).abs();

        // Benutzerdefinierte Parameter des Fraktals werden abgefragt

        parameterRequest = new ParameterRequestEvent();

        adapter.onParameterRequest(parameterRequest);

        FractalView.adapter = adapter;
        homeFragment.initParamSliders();

        // Update UI
        HomeFragment.Companion.onResult(adapter.dimension.getLeft(), adapter.dimension.getTop(), diameter, adapter.itera);
    }

    /**
     * Berechnung im Hintergrund, um Rechenleistung zu verteilen
     */

    @SuppressLint("StaticFieldLeak")
    private class DrawTask extends AsyncTask<Void, Void, Void> {
        private final FractalView fractalView;
        private int taskId;
        private int startWidth = 9; // startWidth (pow(2))
        private int maxWidth = (int) Math.pow(2, startWidth); // startWidth (pow(2))


        public DrawTask(FractalView view) {
            taskId = ++lastTask;
            this.fractalView = view;
        }

        @Override
        protected void onPreExecute() {
            // Zuweisung einer einzigartigen ID, damit dieser Task gestoppt wird, falls ein anderer Task gestartet wird
            if (taskId != lastTask) {
                taskId = lastTask + 2;
                lastTask = taskId;
            }

            Log.i("TASK", "TASK ID " + taskId + " started!");
        }

        @SuppressLint("NewApi")
        @Override
        protected Void doInBackground(Void... size) {

            Paint p = new Paint();

            Dimension tempDimension = adapter.dimension;
            Canvas canvas = fractalView.getCanvas();

            // Berechnung von Dimensionen

            BigDecimal top = tempDimension.getTop();
            BigDecimal bottom = tempDimension.getDown();
            BigDecimal right = tempDimension.getRight();
            BigDecimal left = tempDimension.getLeft();

            // Unterstützung von Bildschirmen, falls das Bild nicht quadratisch sein sollte

            double verhaeltnis = (1 - (((double) canvas.getHeight()) / canvas.getWidth())) * .5;
            BigDecimal customAdd = tempDimension.getDiameter().multiply(BigDecimal.valueOf(verhaeltnis));
            BigDecimal customTop = top.subtract(customAdd);
            BigDecimal customBottom = bottom.add(customAdd);

            // Drawstart event

            adapter.onDrawStart();

            // Zuerst wird alle 32 Pixel ein Funktioswert berechnet, dass jeden 16 usw.

            for (int j = startWidth; j > -1; --j) {

                int width = (int) Math.pow(2, j);

                int altWidth = width / 2;

                Log.i("DRAWSTATUS", width + "");

                int countX = (int) (canvas.getWidth() / width) + 1;
                int countY = (int) (canvas.getHeight() / width) + 1;

                // Log.e("WIDTH", String.valueOf(width));

                if (countX == 0 || countY == 0) continue;

                BigDecimal stepY = initGetPoints(customBottom, customTop, countY);
                BigDecimal stepX = initGetPoints(left, right, countX);

                // Berechnung der Werte für jeden XY Wert, der der Auflösung entspricht

                for (int k = 0; k < countY; k++) {

                    double yCoordinate = getPoint(k, stepY, bottom);
                    int bitMapY = (int) (k * width);
                    adapter.onNewLine();

                    for (int l = 0; l < countX; l++) {

                        double xCoordinate = getPoint(l, stepX, left);
                        double bitMapX = (l * width);


                        if (width != 32)

                            if (l != 0 && (bitMapX % width) + (bitMapY % (width)) == width) {
                                Log.i("Skipping", "Skipped X " + bitMapX + " Y" + bitMapY);
                                continue;
                            }

                        // Abfrage der Farbe am jeweiligen Koordiantenpunkt. Die Funktion, welche die Iteratiosnanzahl berechnet befindet sich im jeweils aktiven Adapter

                        DrawInfo info = new DrawInfo(xCoordinate, yCoordinate, (int) bitMapX, bitMapY);
                        int iteration = adapter.onDraw(info);

/*
                        Einfärbung und Zeichnung nach Iterationswert
*/

                        ColorPalette palette = getPalette();
                        if (adapter.useIntegratedColorPalette())
                            p.setColor(palette.getColorByIteration(iteration));
                        else
                            p.setColor(info.getColor());
                        Rect rect = new Rect(
                                (int) (bitMapX),
                                bitMapY,
                                (int) (bitMapX + width),
                                (int) (bitMapY + width)
                        );
                        canvas.drawRect(rect, p);
                    }
                    if (taskId != lastTask) return null;
                    publishProgress();

                  /*  try {
//                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                }


            }

            return null;
        }


        @Override
        protected void onProgressUpdate(Void... values) {

            invalidate();
        }

        @Override
        protected void onPostExecute(Void unused) {

        }
    }

    public static ParameterRequestEvent getParameterRequest() {
        return parameterRequest;
    }

    // Klasse, um seine eigenen Fraktale einzubinden
    public abstract static class Adapter {

        public int itera = 100;

        public double getParam(String id) {

            ParameterRequestEvent.Parameter param = FractalView.getParameterRequest().getParameterRequests().get(id);

            return param != null ? param.getValue() : 0;
        }

        public void onNewLine() {
        }

        public void onDrawStart() {
        }

        protected Dimension dimension;

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
         * @return Iteration der divergenz des zu zeichnenden Pixels (Bsp. Mandelbrotmenge = 2)
         */
        public abstract int onDraw(DrawInfo info);

        /**
         * @return die Größe des KoordinatenSystems
         */
        public abstract Dimension getInitialSize();

        public Dimension getDimension() {
            return dimension;
        }

        public boolean useIntegratedColorPalette() {
            return true;
        }

        public void onParameterRequest(ParameterRequestEvent e) {
        }
    }

}
