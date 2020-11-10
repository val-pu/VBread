package val.mx.vbread.views;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.ColorSpace;

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

    @Override
    public DrawInfo onDraw(DrawInfo info) {

        int check = 3, checkCounter = 0;
        int update = 10, updateCounter = 0;
        VComplex comp = new VComplex(info.getX().doubleValue(), info.getY().doubleValue());
        VComplex start = comp;
        int pow = 1;
        for (int i = 0; i < itera; i++) {


            comp = comp.multiply(comp).add(start);

            // Farben https://www.math.univ-toulouse.fr/~cheritat/wiki-draw/index.php/Mandelbrot_set
            // Farben https://www.codingame.com/playgrounds/2358/how-to-plot-the-mandelbrot-set/adding-some-colors

            if (comp.abs() > 2) {

//                int f = (int) ((i + 1 - Math.log( (int) Math.log(comp.abs()) /*/ Math.log(2)*/)) * 255F/itera);
                float f = (float) (i + 1 - Math.log(log2(comp.abs()))) * (255F / itera);


                int color = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    info.setColor(Color.valueOf(f, f, f).toArgb());
//
//                    color = colors[i % colors.length];
//
//                        if(i!= 0)
//                        color = color +  colors[(i+1)%colors.length]/ i%itera;
//
//                    info.setColor(color);
                }
                return info;
            }

            pow *= 2;

            // PERIODICITY CHECKING
            // https://en.wikipedia.org/wiki/User:Simpsons_contributor/periodicity_checking
            if (Math.abs(old.getImag() - comp.getImag()) < ZERO)
                if (Math.abs(old.getReal() - comp.getReal()) < ZERO) {

                    info.setColor(Color.WHITE);

                    return info;
                }

            if (check == checkCounter) {
                checkCounter = 0;
                old = comp;

                if (update == updateCounter) {
                    check *= 2;
                    updateCounter = 0;
                }

                updateCounter++;
            }

            checkCounter++;
        }
        info.setColor(Color.WHITE);
        return info;
    }

    @SuppressLint("NewApi")
    public int bildeMitteFrabe(int c1, int c2) {

        if (c1 == c2) return c1;

        return ((int) (Math.abs(c1 - c2) / 1.1D)) + c2;
    }

    private int log2(Double val) {
        return (int) (Math.log(val) / Math.log(2));
    }

    @Override
    public void onNewLine() {
        old = new VComplex(9D, 9D);
    }

    @Override
    public Dimension getSize() {
        return dimension;
    }

    private Double ZERO = 1e-30;

}
