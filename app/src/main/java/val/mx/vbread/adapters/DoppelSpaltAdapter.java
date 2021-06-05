package val.mx.vbread.adapters;

import android.graphics.Paint;
import android.util.Log;

import java.math.BigDecimal;

import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.views.FractalView;
import val.mx.vbread.views.ParameterRequestEvent;

public class DoppelSpaltAdapter extends FractalView.Adapter {

    public DoppelSpaltAdapter() {
        b1 = new Brennpunkt(0,10,200);
        b2 = new Brennpunkt(0,-10,200);
    }

    @Override
    public void onParameterRequest(ParameterRequestEvent e) {
        e.request("lambda1",0.1,200);
        e.request("abstand",0.1,200);
        super.onParameterRequest(e);
        Log.i("LAMDA",b1.lamda + "");
    }

    @Override
    public void onDrawStart() {

        double abstand = getParam("abstand");

        b1 = new Brennpunkt(0,abstand,0);
        b1 = new Brennpunkt(0,-abstand,0);
        b1.lamda = (int) getParam("lambda2");
        b2.lamda = b1.lamda;
        Log.i("LAMDA",b1.lamda + "");
        if(b1.lamda == 0) b1.lamda = 200;
    }

    @Override
    public int onDraw(DrawInfo info) {
        double i = info.getX();
        double j = info.getY();

        Vector v = new Vector(b1,i,j);
        Vector v2 = new Vector(b2,i,j);

        double pi2 = Math.PI*2;

        double deltaLambda = Math.cos((v.abs()/b1.lamda)*pi2)+ Math.cos(pi2*(v2.abs()/b2.lamda));

        deltaLambda %= b1.lamda;

        Paint p = new Paint();
        int c = (int) Math.abs(122.5*deltaLambda);

        p.setARGB(255,c,c,255);

        info.setColor(p.getColor());
        return 0;
    }

    @Override
    public Dimension getInitialSize() {
        return new Dimension(new BigDecimal("-20"), new BigDecimal("20"), new BigDecimal("20"), new BigDecimal("-20"));
    }

    private class Brennpunkt {
        private final double x;
        private final double y;
        private double lamda;

        Brennpunkt(int x, double y, double lamda) {
            this.x = x;
            this.y = y;
            this.lamda = lamda;
        }

        public double getLamda() {
            return lamda;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    private class Vector {
        double x,y;
        public Vector(Brennpunkt b, double x, double y) {
            this.x = b.getX()-x;
            this.y = b.getY()-y;
        }

        public double abs() {
            return Math.sqrt(x*x+y*y);
        }

    }
    Brennpunkt b1, b2;

    @Override
    public boolean useIntegratedColorPalette() {
        return false;
    }
}
