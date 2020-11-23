package val.mx.vbread;

public class VComplex {
    private final Double real;
    private final Double imag;

    public VComplex(Double real, Double imag) {
        this.real = real;
        this.imag = imag;
    }

    public Double getImag() {
        return imag;
    }

    public Double getReal() {
        return real;
    }

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

    public VComplex divide(VComplex div) {
        return new VComplex(
                (real * div.getReal() + imag * div.getImag()) / (Math.pow(div.getReal(), 2) + Math.pow(div.getImag(), 2)), (imag * div.getReal() - real * div.getImag()) / (Math.pow(div.getReal(), 2) + Math.pow(div.getImag(), 2))
        );
    }

    public VComplex pow(int fac) {
        VComplex temp = this;
        for (int i = 0; i < fac; i++) {
            temp = temp.multiply(this);
        }
        return temp;
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


    // AB HIER "Kopiert" https://www.math.ksu.edu/~bennett/jomacg/c.html
    // TODO: 22.11.2020 Verstehen

    // Real cosh function (used to compute complex trig functions)
    private double cosh(double theta) {
        return (Math.exp(theta) + Math.exp(-theta)) / 2;
    }

    // Real sinh function (used to compute complex trig functions)
    private double sinh(double theta) {
        return (Math.exp(theta) - Math.exp(-theta)) / 2;
    }

    /**
     * Sine of this Complex number (doesn't change this Complex number).
     * <br>sin(z) = (exp(i*z)-exp(-i*z))/(2*i).
     *
     * @return sin(z) where z is this Complex number.
     */
    public VComplex sin() {
        return new VComplex(cosh(imag) * Math.sin(real), sinh(imag) * Math.cos(real));
    }

    /**
     * Cosine of this Complex number (doesn't change this Complex number).
     * <br>cos(z) = (exp(i*z)+exp(-i*z))/ 2.
     *
     * @return cos(z) where z is this Complex number.
     */
    public VComplex cos() {
        return new VComplex(cosh(imag) * Math.cos(real), -sinh(imag) * Math.sin(real));
    }


}
