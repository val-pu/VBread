package val.mx.vbread.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

public class Doppelspaltview extends androidx.appcompat.widget.AppCompatImageView {

    private class Brennpunkt {
        private final int x;
        private final int y;
        private final int lamda;

        Brennpunkt(int x, int y, int lamda) {
            this.x = x;
            this.y = y;
            this.lamda = lamda;
        }

        public int getLamda() {
            return lamda;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    private class Vector {
        int x,y;
        public Vector(Brennpunkt b, int x, int y) {
            this.x = b.getX()-x;
            this.y = b.getY()-y;
        }

        public double abs() {
            return Math.sqrt(x*x+y*y);
        }

    }
    Brennpunkt b1, b2;
    private Bitmap bm;
    private Canvas canvas;

    public Doppelspaltview(@NonNull @NotNull Context context, @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
        super(context, attrs);
        post(() -> {
            b1 = new Brennpunkt(20,100,200);
            b2 = new Brennpunkt(20,1000,200);
            bm = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.RGB_565);
            canvas = new Canvas(bm);
            setBackground(new BitmapDrawable(bm));

            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    Vector v = new Vector(b1,i,j);
                    Vector v2 = new Vector(b2,i,j);

                    double pi2 = Math.PI*2;

                    double deltaLambda = Math.cos(v.abs()*pi2)+ Math.cos(pi2*v2.abs());

                    deltaLambda %= b1.lamda;

                    Rect r = new Rect();
                    r.set(i,j,i + 1,j + 1);
                    Paint p = new Paint();

                    int c = (int) (255*deltaLambda);
                    p.setARGB(c,c,c,c);

                    canvas.drawRect(r,p);


                }
            }

        });
    }

}
