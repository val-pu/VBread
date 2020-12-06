package val.mx.vbread.adapters;

import java.math.BigDecimal;

import val.mx.vbread.Complex;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.views.FractalView;

import static android.graphics.Color.BLACK;

// Formel z = z^2 + 2* c

public class ModdedBrotAdapter extends FractalView.Adapter {



    @Override
    public int onDraw(DrawInfo info) {
        Complex c = new Complex(info.getX(), info.getY());
        Complex z = c;

        for (int i = 0; i < itera; i++) {

            z = z.multiply(z).add(c.multiply(2));

            if (z.abs() > 2) return i;

        }

        info.setColor(BLACK);
        return -1;
    }


    @Override
    public Dimension getInitialSize() {
        return new Dimension(new BigDecimal("-2"), new BigDecimal("2"), new BigDecimal("2"), new BigDecimal("-2"));
    }
}
