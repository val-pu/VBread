package val.mx.vbread.adapters;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;

import val.mx.vbread.Complex;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.views.FractalView;
import val.mx.vbread.views.ParameterRequestEvent;

public class ParameterizedMandelBrotAdapter extends FractalView.Adapter {


    @Override
    public void onParameterRequest(ParameterRequestEvent e) {
        e.request("param",-2,2);
    }

    private Complex old = new Complex(20D, 20D);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onDraw(DrawInfo info) {

        Complex param = new Complex(getParam("param"),.1D);

        int threshold = (int) (itera/20D);

        int check = 3, checkCounter = 0;
        int update = 10, updateCounter = 0;
        Complex c = new Complex(info.getX(), info.getY());
        Complex start = c;

        for (int i = 0; i < itera; i++) {


            c = c.multiply(c).add(start).add(param);

            // Farben https://www.math.univ-toulouse.fr/~cheritat/wiki-draw/index.php/Mandelbrot_set
            // Farben https://www.codingame.com/playgrounds/2358/how-to-plot-the-mandelbrot-set/adding-some-colors

            if (c.abs() > 2) {

                if(i < threshold) {
                    return i;
                }
                return i;
            }

            // PERIODICITY CHECKING
            // https://en.wikipedia.org/wiki/User:Simpsons_contributor/periodicity_checking
            if (Math.abs(old.getImag() - c.getImag()) < ZERO)
                if (Math.abs(old.getReal() - c.getReal()) < ZERO) {

                    info.setColor(Color.BLACK);

                    return -1;
                }

            if (check == checkCounter) {
                checkCounter = 0;
                old = c;

                if (update == updateCounter) {
                    check *= 2;
                    updateCounter = 0;
                }

                updateCounter++;
            }

            checkCounter++;
        }
        info.setColor(Color.BLACK);
        return -1;
    }

    @Override
    public void onNewLine() {
        old = new Complex(9D, 9D);
    }

    @Override
    public Dimension getInitialSize() {
        return new Dimension(new BigDecimal("-2"),new BigDecimal("2"),new BigDecimal("2"),new BigDecimal("-2"));
    }

    private Double ZERO = 1e-30;

}
