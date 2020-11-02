package val.mx.vbread.views;

import android.graphics.Color;
import android.util.Log;

import java.math.BigDecimal;

import val.mx.vbread.VComplex;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;

public class MandelBrotAdapter extends FractalView.Adapter {

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


    private VComplex old = new VComplex(20D, 20D);
    private DrawInfo oldInfo = new DrawInfo(new BigDecimal(0), new BigDecimal(0), 1, 1);
    private Double akzeptanz = 0.000003D;

    @Override
    public DrawInfo onDraw(DrawInfo info) {


        VComplex comp = new VComplex(info.getX().doubleValue(), info.getY().doubleValue());
        VComplex tempC = comp;
        VComplex start = comp;


        for (int i = 0; i < itera; i++) {


            comp = comp.multiply(comp).add(start);
            if (comp.abs() > 2) {
                info.setColor(colors[i % colors.length]);
                return info;
            }

            if (Math.abs(Math.abs(old.abs() - comp.abs())) < akzeptanz) {
                info.setColor(Color.WHITE);
                return info;
            }
        }
        old = tempC;
        info.setColor(Color.WHITE);
        return info;
    }


    @Override
    public Dimension getSize() {
        return dimension;
    }

}
