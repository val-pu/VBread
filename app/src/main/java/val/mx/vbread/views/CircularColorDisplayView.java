package val.mx.vbread.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;

import java.util.LinkedList;

public class CircularColorDisplayView extends androidx.appcompat.widget.AppCompatImageView {
    private int color;
    private Canvas canvas;

    public CircularColorDisplayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    public void setData(Integer color) {
        this.color = color;
        init();
    }


    private void init() {

        if (color == 0) {
            Log.w("CColorDisplayView", "Data size is 0! Stopping draw process..");
            return;
        }


        if (getHeight() == 0) {
            Log.w("CColorDisplayView", "Width is 0! Did you forget to wait for layout? Stopping draw process..");
            return;
        }

        int height = getHeight();

        Bitmap bitmap = Bitmap.createBitmap(getHeight(), getHeight(), Bitmap.Config.ARGB_8888);
        setBackgroundDrawable(new BitmapDrawable(bitmap));
        invalidate();
        canvas = new Canvas(bitmap);

        Paint p = new Paint();

        p.setColor(color);

        canvas.drawCircle(height/2f, height/2f, (float) (getHeight() / 2D), p);

        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    private void initSampleData() {
        setData(Color.BLACK);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
    }
}
