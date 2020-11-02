package val.mx.vbread.views;

import android.graphics.Color;

import val.mx.vbread.Complex;
import val.mx.vbread.VComplex;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;

public class JuliaBrotAdapter extends FractalView.Adapter {

    VComplex param = new VComplex(0.1, -0.1D);

    public void setParam(VComplex param) {
        this.param = param;
    }

    int[] colors = new int[]{
            Color.rgb(9, 1, 47),
            Color.rgb(4, 4, 73),
            Color.rgb(0, 7, 100),
            Color.rgb(12, 44, 138),
            Color.rgb(24, 82, 177),
            Color.rgb(57, 125, 209),
            Color.rgb(134, 181, 229),
//            Color.rgb(211, 236, 248),
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


        VComplex complex = new VComplex(info.getX().doubleValue(), info.getY().doubleValue());
        for (int i = 0; i < 1000; i++) {
            complex = complex.multiply(complex).add(param);
            if (complex.abs() > 2) {
                info.setColor(colors[i % colors.length]);
                return info;
            }

        }
        info.setColor(Color.WHITE);

        return info;
    }

    @Override
    public Dimension getSize() {
        return dimension;
    }


}
