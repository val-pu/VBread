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

public class ColorDisplayView extends androidx.appcompat.widget.AppCompatImageView {
    private LinkedList<Integer> colors;
    private Canvas canvas;

    public ColorDisplayView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    public void setData(LinkedList<Integer> colors) {
        this.colors = colors;
        init();
    }


    private void init() {
        if (colors == null) {
            Log.w("ColorDisplayView","Data is null! Stopping draw process..");
            return;
        }

        if (colors.size() == 0) {
            Log.w("ColorDisplayView","Data size is 0! Stopping draw process..");
            return;
        }

        int width = getWidth();
        if (width == 0) {
            Log.w("ColorDisplayView","Width is 0! Did you forget to wait for layout? Stopping draw process..");
            return;
        }

        Bitmap bitmap = Bitmap.createBitmap(width,getHeight(), Bitmap.Config.ARGB_8888);
        setBackgroundDrawable(new BitmapDrawable(bitmap));
        invalidate();
        canvas = new Canvas(bitmap);

        float step = ((float) width) / colors.size();

        Paint p = new Paint();

        for (int i = 0; i < colors.size(); i++) {
            int color = colors.get(i);

            p.setColor(color);

            float start = step* i;

            RectF rect = new RectF(start,0,start+step,getHeight());
            Log.i("I",rect.toString());

            canvas.drawRect(rect,p);
        }

        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void initSampleData() {
        colors = new LinkedList<>();
        colors.add(Color.RED);
        colors.add(Color.YELLOW);
        colors.add(Color.GRAY);
        colors.add(Color.GREEN);
        colors.add(700);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
    }
}
