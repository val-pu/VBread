package val.mx.vbread.adapters;

import android.graphics.Color;

import java.math.BigDecimal;

import val.mx.vbread.VComplex;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.views.FractalView;

import static android.graphics.Color.BLACK;

// Formel z = z^2 + 2* c

public class ModdedBrotAdapter3 extends FractalView.Adapter {
    int[] colors = new int[]{
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
    };


    @Override
    public DrawInfo onDraw(DrawInfo info) {
        VComplex c = new VComplex(info.getX(), info.getY());
        VComplex z = c;

        for (int i = 0; i < itera; i++) {

            z = z.multiply(z).multiply(z).add(c);

            if (z.abs() > 2) return plot(i, info);

        }

        info.setColor(BLACK);
        return info;
    }

    private DrawInfo plot(int i, DrawInfo inf) {
        inf.setColor(colors[i % colors.length]);
        return inf;
    }

    @Override
    public Dimension getInitialSize() {
        return new Dimension(new BigDecimal("-2"), new BigDecimal("2"), new BigDecimal("2"), new BigDecimal("-2"));
    }
}
