package val.mx.vbread;

import val.mx.vbread.containers.DrawInfo;

public class VComplex {
    private final Double real;
    private final Double imag;

    public VComplex(Double real, Double imag) {
        this.real = real;
        this.imag = imag;
    }

//    public VComplex(Double real, Double imag) {
//        this.real = new Double(real);
//        this.imag = new Double(imag);
//    }

    public Double abs() {

        // WURZELBERECHNUNG https://de.wikipedia.org/wiki/Newtonverfahren
        // TODO: 02.11.2020

        double decimal = Math.pow(real, 2) + Math.pow(imag, 2);
        return Math.sqrt(decimal);
    }

    public VComplex multiply(VComplex complex) {
        return new VComplex(
                real * complex.real - imag * complex.imag,
                imag * complex.real + imag * complex.real);
    }

    public VComplex multiply(int fac) {
        return new VComplex(real * fac, imag * fac);
    }

    public VComplex add(Double real) {
        return new VComplex(this.real + real, imag);
    }

    public VComplex add(VComplex toAdd) {
        return new VComplex(real + toAdd.real, imag + toAdd.imag);
    }

    public VComplex subtract(Double real) {
        return new VComplex(this.real - real, imag);
    }

    public VComplex subtract(VComplex toAdd) {
        return new VComplex(real - toAdd.real, imag - toAdd.imag);
    }
}
