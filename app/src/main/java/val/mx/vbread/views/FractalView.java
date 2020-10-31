package val.mx.vbread.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;

import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;

public class FractalView extends androidx.appcompat.widget.AppCompatImageView {
    private int lastTask = 0;
    private Adapter adapter = null;
    private Bitmap bitmap;
    private int resolution = 2, resolutionExtra;
    private Canvas canvas;
    private Dimension dimension;
    private BigDecimal verhaeltnis;

    public FractalView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);
        if (changed) return;
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
            taskId = ++lastTask;
        }

        @Override
        protected LinkedList<DrawInfo> doInBackground(Integer... size) {

            Log.i("Zeichnen", "Frage Farben ab und setze Pixel");

            Canvas canvas = fractalView.getCanvas();


            int lastResolution = resolution + 1;
            int currentResolution = resolution;

            int teiler = 1;
            for (int i = resolution - 1; i > 3; i--) {
                if (resolution % i == 0) {
                    teiler = i;
                }
            }
            Log.e("TEILER", teiler + " :)");
            int max = 20;
//            wurzel:
//            for (int r = 2; r < max; r++) {
//                int x = (int) (1F * getWidth() / r);
//                currentResolution = r;
//                for (int i = 0; i < r; i++) {
//
//
//
//                    for (int j = 0; j < r; j++) {
//
//                        if (taskId != lastTask) break wurzel;
//
//
//                        DrawInfo inf = new DrawInfo(getPoint(dimension.getLeft(), dimension.getRight(), currentResolution, j), y, Integer.valueOf(j * x).shortValue(), Integer.valueOf(i * x).shortValue());
//                        inf = fractalView.getAdapter().onDraw(inf);
//                        RectF rectF = new RectF(inf.getScreenX(), inf.getScreenY(), inf.getScreenX() + x, inf.getScreenY() + x);
//                        p.setColor(inf.getColor());
//                        canvas.drawRect(rectF, p);
//
//                    }
//                    invalidate();
//
//                }
                int lasti = 1000000;

                for (int i = 80; 1 < i; i--) {
                    int width = i;

                    for (int j = 0; j < i-getWidth()/80; j++) {


                        BigDecimal y = getPoint(dimension.getLeft(), dimension.getRight(), i, j);
                        for (int k = 0; k < i-getWidth()/80; k++) {


                            DrawInfo inf = new DrawInfo(getPoint(dimension.getLeft(), dimension.getRight(), i, k), y,  k *(int) width, j * (int)width);

                            inf = fractalView.getAdapter().onDraw(inf);
                            if(inf.getScreenX()%lasti == 0) continue ;

                            RectF rectF = new RectF(inf.getScreenX(), inf.getScreenY(), inf.getScreenX() + width, inf.getScreenY() + width);
                            p.setColor(inf.getColor());
                            canvas.drawRect(rectF, p);

                        }
                        invalidate();

                    }
                    lasti = (int) width;
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
