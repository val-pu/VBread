package val.mx.vbread.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Random;

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

    private int lastTask = 1;
    private int lastZoomDistance = 0;
    public static Adapter adapter = new MandelBrotAdapter();
    private Bitmap bitmap;
    private Canvas canvas;
    public HomeFragment homeFragment;
    private BigDecimal ratio;
    private ColorPalette palette;

    public FractalView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        palette = new ColorPalette("Standard", Color.BLACK,
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

        super.onLayout(changed, left, top, right, bottom);
        if (canvas == null)
            getRootView().post(() -> {
                bitmap = Bitmap.createBitmap(
                        getMeasuredWidth(),
                        getMeasuredHeight(),
                        Bitmap.Config.ARGB_8888);

                canvas = new Canvas(bitmap);
                setBackgroundDrawable(new BitmapDrawable(bitmap));
                adapter = new MandelBrotAdapter();
                init();
            });
        else {

            if (bitmap.getHeight() == getHeight()) return;
            bitmap = Bitmap.createBitmap(
                    getMeasuredWidth(),
                    getMeasuredHeight(),
                    Bitmap.Config.ARGB_8888);

            canvas = new Canvas(bitmap);
            setBackgroundDrawable(new BitmapDrawable(bitmap));
            setAdapter(adapter);
        }
    }


    public void init() {

        if (adapter.getDimension() == null) adapter.setDimension(adapter.getInitialSize());


        /*ratio = new BigDecimal(getMeasuredWidth()).divide(new BigDecimal(getMeasuredHeight()), 3, RoundingMode.DOWN);
        Adapter.verhaeltnis = ratio;*/

        Log.i("Zeichnen", "Zeichenprozess hat begonnen Dimension ist " + adapter.dimension);
        DrawTask task;
        task = new DrawTask(this, 1, 1, true);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        task = new DrawTask(this,2,2,false);
//        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        task = new DrawTask(this,2,2,false);
//        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        task = new DrawTask(this,2,2,true);
//        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        task = new DrawTask(this,3,3,false);
//        task.execute();

    }

    @SuppressLint("Assert")
    public void setAdapter(Adapter adapter) {
        assert getWidth() != 0;
        FractalView.adapter = adapter;
        init();
    }

    private BigDecimal step, low;

    public BigDecimal initGetPoints(BigDecimal low, BigDecimal high, int count) {
        this.low = low;
        BigDecimal combinedRange = high.subtract(low).abs();
        return combinedRange.divide(new BigDecimal(count), 40, RoundingMode.DOWN);
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
//                adapter.dimension = dimension;

                    if (touchCount % 4 == 0) {
                        touchCount = 0;
                        HomeFragment.Companion.onResult(adapter.dimension.getLeft(), adapter.dimension.getTop(), adapter.dimension.getDiameter(), adapter.itera);

                    }
                    x1 = event.getX();
                    y1 = event.getY();
                    break;
                }
                case MotionEvent.ACTION_UP:
                    x1  = y1 = 0;
                    HomeFragment.Companion.onResult(adapter.dimension.getLeft(), adapter.dimension.getTop(), adapter.dimension.getDiameter(), adapter.itera);

            }
        }
        y1 = event.getY();
        x1 = event.getX();
        lastEvent = event;
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

        FractalView.adapter = adapter;

        HomeFragment.Companion.onResult(adapter.dimension.getLeft(), adapter.dimension.getTop(), diameter, adapter.itera);
    }

    /**
     * Berechnung im Hintergrund, um Rechenleistung zu verteilen
     */
    private static int redrawStatus = 0;

    @SuppressLint("StaticFieldLeak")
    private class DrawTask extends AsyncTask<Void, Void, Void> {
        private double yAdd;
        double dy;
        private FractalView fractalView;
        private int taskId = -1;


        public DrawTask(FractalView view, int teiler, int i, boolean newId) {
            if (!newId)
                taskId = lastTask;
            else redrawStatus = 0;
            double step = (getWidth() / ((double) teiler));


            this.yAdd = step * (i - 1);
            this.dy = step * (i);
            this.fractalView = view;

            System.out.printf("yd %s Yadd %s", dy, yAdd);
        }

        @Override
        protected void onPreExecute() {
            // Zuweisung einer einzigartigen ID
            if (taskId != lastTask) {
                taskId = lastTask + 2;
                lastTask = taskId;
            }

            Log.i("TASK", "TASK ID " + taskId + " started!");
        }

        private int resoloutionX = getWidth();
        private int resoloutionY = getHeight();

        @SuppressLint("NewApi")
        @Override
        protected Void doInBackground(Void... size) {

            Paint p = new Paint();

            Dimension tempDimension = adapter.dimension;
            Canvas canvas = fractalView.getCanvas();

            BigDecimal top = tempDimension.getTop();
            BigDecimal bottom = tempDimension.getDown();
            BigDecimal right = tempDimension.getRight();
            BigDecimal left = tempDimension.getLeft();

            // Anpassung, um Mobiltelefone verschiedener Höhen zu unterstützen

            double verhaeltnis = (((double) canvas.getHeight()) / canvas.getWidth());

            BigDecimal customAdd = BigDecimal.ZERO;

            BigDecimal customTop = top.add(customAdd);
            BigDecimal customBottom = bottom.subtract(customAdd);

            int lastWidth = 45000;

            for (int j = 6; j > 0; j--) {

                int width = (int) Math.pow(2, j);

                int countX = getWidth() / width;
                int countY = getHeight() / width;
                Log.e("WIDTH", String.valueOf(width));

                BigDecimal stepY = initGetPoints(customBottom, customTop, countY);
                BigDecimal stepX = initGetPoints(left, right, countX);

                for (int k = 0; k < countY; k++) {


                    double yCoordinate = getPoint(k, stepY, bottom);
                    int bitMapY = k * width;

                    /*if (k != 0 && bitMapY % lastWidth == 0) {
                        continue;
                    }*/


                    adapter.onNewLine();

                    for (int l = 0; l < countX; l++) {

                        double xCoordinate = getPoint(l, stepX, left);
                        int bitMapX = l * width;

                        if (l != 0 && bitMapX % lastWidth == 0) {
                            continue;
                        }

                        DrawInfo info = new DrawInfo(xCoordinate, yCoordinate, bitMapX, bitMapY);
                        int iteration = adapter.onDraw(info);

                        ColorPalette palette = getPalette();
                        p.setColor(palette.getColorByIteration(iteration));

                        Rect rect = new Rect(
                                bitMapX,
                                bitMapY,
                                bitMapX + width,
                                bitMapY + width
                        );

                        canvas.drawRect(rect, p);
                        invalidate();
                    }
                    if (taskId != lastTask) return null;
                }

                lastWidth = width;

            }

            return null;
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

        public boolean usesIntegratedColorPalette() {
            return true;
        }

    }

}
