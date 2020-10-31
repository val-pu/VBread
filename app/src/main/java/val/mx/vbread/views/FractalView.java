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
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;

import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;

public class FractalView extends androidx.appcompat.widget.AppCompatImageView {

    private Adapter adapter = null;
    private Bitmap bitmap;
    private int resolution = 2, resolutionExtra;
    private Canvas canvas;
    private Dimension dimension;
    private BigDecimal verhaeltnis;

    public FractalView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init() {
        bitmap = Bitmap.createBitmap(
                getMeasuredWidth(),
                getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        setBackgroundDrawable(new BitmapDrawable(bitmap));


        dimension = adapter.getSize();

        verhaeltnis = new BigDecimal(getMeasuredWidth()).divide(new BigDecimal(getMeasuredWidth()), 3, RoundingMode.DOWN);
        Adapter.verhaeltnis = verhaeltnis;

        Log.i("Verhältnis", "Nutze das Verhältnis 1 : " + verhaeltnis);

        DrawInfo[][] v = new DrawInfo[getMeasuredWidth() / resolution][getMeasuredWidth() / resolution];

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

        public DrawTask(FractalView view) {
            this.fractalView = view;
        }

        @Override
        protected LinkedList<DrawInfo> doInBackground(Integer... size) {

            Log.i("Zeichnen", "Frage Farben ab und setze Pixel");

            Canvas canvas = fractalView.getCanvas();

            int x = getWidth()/resolution;

            for (int i = 0; i < getWidth()/resolution; i++) {


                BigDecimal y = getPoint(dimension.getLeft(), dimension.getRight(), x, i);

                for (int j = 0; j < getWidth()/resolution; j++) {
                    short k = 1;
                    DrawInfo inf = new DrawInfo(getPoint(dimension.getLeft().divide(verhaeltnis, 20, RoundingMode.DOWN), dimension.getRight().divide(verhaeltnis, 20, RoundingMode.DOWN), x, j), y, Integer.valueOf(j * resolution).shortValue(), Integer.valueOf(i * resolution).shortValue());
                    inf = fractalView.getAdapter().onDraw(inf);
                    RectF rectF = new RectF(inf.getScreenX(), inf.getScreenY(), inf.getScreenX() + resolution, inf.getScreenY() + resolution);
                    p.setColor(inf.getColor());
                    canvas.drawRect(rectF, p);

                }
                invalidate();
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
