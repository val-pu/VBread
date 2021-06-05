package val.mx.vbread.adapters;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;

import val.mx.vbread.Complex;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.views.FractalView;

public class MandelBrotAdapter extends FractalView.Adapter {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onDraw(DrawInfo info) {

        Complex c = new Complex(info.getX(), info.getY());
        Complex start = c;

        for (int i = 0; i < itera; i++) {

            // Funktion des Fraktals
            c = c.multiply(c).add(start);

            // Abbruch der Berechnung bei Divergenz
            if (c.abs() > 2) {
                return i;
            }
        }

        // Zahl wahrscheinlich in der Menge
        return -1;
    }

    private long start = 0;
    private Long sum = Long.valueOf(0L);
    private int iteras;

    @Override
    public void onNewLine() {
        long time = start - System.nanoTime();
        sum += time;
        iteras++;
//        Log.i("MbrotAdapter", "Took " + time + " for one line MEAN: " + sum / iteras);
        start = System.nanoTime();
    }

    @Override
    public Dimension getInitialSize() {
        return new Dimension(new BigDecimal("-2"), new BigDecimal("2"), new BigDecimal("2"), new BigDecimal("-2"));
    }
}
