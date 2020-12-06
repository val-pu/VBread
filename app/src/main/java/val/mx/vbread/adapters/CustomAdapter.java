package val.mx.vbread.adapters;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.math.BigDecimal;

import val.mx.vbread.Complex;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.views.FractalView;

public class CustomAdapter extends FractalView.Adapter {

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


    private Complex old = new Complex(20D, 20D);

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onDraw(DrawInfo info) {

        int threshold = (int) (itera / 20D);

        int check = 3, checkCounter = 0;
        int update = 10, updateCounter = 0;
        Complex c = new Complex(info.getX(), info.getY());
        Complex start = c;

        for (int i = 0; i < itera; i++) {


            c = c.multiply(c.multiply(0.3)).subtract(start).add(new Complex(1D,1D));

            // Farben https://www.math.univ-toulouse.fr/~cheritat/wiki-draw/index.php/Mandelbrot_set
            // Farben https://www.codingame.com/playgrounds/2358/how-to-plot-the-mandelbrot-set/adding-some-colors

            if (c.abs() > 4) {
                return i;
            }
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

        return -1;
    }

    @Override
    public void onNewLine() {
        old = new Complex(9D, 9D);
    }

    @Override
    public Dimension getInitialSize() {
        return new Dimension(new BigDecimal("-2"), new BigDecimal("2"), new BigDecimal("2"), new BigDecimal("-2"));
    }

    private Double ZERO = 1e-30;

}
