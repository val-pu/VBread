package val.mx.vbread.adapters;

import android.graphics.Color;

import java.math.BigDecimal;

import val.mx.vbread.Complex;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.views.FractalView;
import val.mx.vbread.views.ParameterRequestEvent;

public class JuliaBrotAdapter extends FractalView.Adapter {

    Complex param = new Complex(0.1, -0.1D);

    public void setParam(Complex param) {
        this.param = param;
    }


    @Override
    public void onDrawStart() {
        double real = getParam("Julia-X");
        double imag = getParam("Julia-Y");

        param = new Complex(real, imag);
    }

    @Override
    public void onParameterRequest(ParameterRequestEvent e) {
        e.request("Julia-X", -2, 2);
        e.request("Julia-Y", -2, 2);
    }

    @Override
    public int onDraw(DrawInfo info) {


        Complex complex = new Complex(info.getX(), info.getY());
        for (int i = 0; i < itera; i++) {
            complex = complex.multiply(complex).add(param);
            if (complex.abs() > 2) {
                return i;
            }

        }
        info.setColor(Color.WHITE);

        return -1;
    }

    @Override
    public Dimension getInitialSize() {
        return new Dimension(new BigDecimal("-2"), new BigDecimal("2"), new BigDecimal("2"), new BigDecimal("-2"));
    }


}
