package val.mx.vbread;

import java.math.BigDecimal;

public class VComplex {
    private final BigDecimal real;
    private final BigDecimal imag;
    private BigDecimal realPart;
    private BigDecimal imagPart;

    public VComplex(BigDecimal real, BigDecimal imag) {
        this.real = real;
        this.imag = imag;
    }

    public BigDecimal abs() {

        // WURZELBERECHNUNG https://de.wikipedia.org/wiki/Newtonverfahren

        BigDecimal decimal =  real.pow(2).add(imag.pow(2));


        for (int i = 0; i < 4; i++) {
        }

        return null;
    }


}
