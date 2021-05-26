package val.mx.vbread.adapters;

import java.math.BigDecimal;

import val.mx.vbread.Complex;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.views.FractalView;

public class Magnet2 extends FractalView.Adapter {
    @Override
    public int onDraw(DrawInfo info) {

        Complex c = new Complex(info.getX(), info.getY());
        Complex z = c;

        for (int i = 0; i < itera; i++) {
            // Große Funktion
            z = z.multiply(z).multiply(z).add(c.subtract(1).multiply(3).multiply(z)).add(c.subtract(1).multiply(c.subtract(2))).divide(z.multiply(z).multiply(3).add(c.subtract(2).multiply(3).multiply(z)).add(c.subtract(1).multiply(c.subtract(2))).add(1)).pow(2);
            if (z.abs() > 20) return i;
        }

        return -1;
    }

    @Override
    public Dimension getInitialSize() {
        return new Dimension(new BigDecimal("-2"), new BigDecimal("2"), new BigDecimal("2"), new BigDecimal("-2"));
    }
}
