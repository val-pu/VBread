package val.mx.vbread;

public class Complex {
    private final Double real;
    private final Double imag;

    public Complex(Double real, Double imag) {
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

    public Complex conjugate() {
        return new Complex(real,-imag);
    }

    public Complex multiply(Complex complex) {
        return new Complex(
                real * complex.real - imag * complex.imag,
                imag * complex.real + real * complex.imag);
    }

    public Complex multiply(double fac) {
        return new Complex(real * fac, imag * fac);
    }

    public Complex divide(Complex div) {
        System.out.println("1: " + div.conjugate());
        System.out.println("2: " + (Math.pow(div.imag,2)+Math.pow(div.real,2)));
        return multiply(div.conjugate()).divide(Math.pow(div.imag,2)+Math.pow(div.real,2));
    }

    public Complex divide(double fac) {

        return new Complex(real/fac,imag/fac);
    }

    public Complex pow(int fac) {
        Complex temp = this;
        for (int i = 0; i < fac; i++) {
            temp = temp.multiply(this);
        }
        return temp;
    }

    public Complex add(Double real) {
        return new Complex(this.real + real, imag);
    }

    public Complex add(Complex toAdd) {
        return new Complex(real + toAdd.real, imag + toAdd.imag);
    }

    public Complex subtract(Double real) {
        return new Complex(this.real - real, imag);
    }

    public Complex subtract(Complex toAdd) {
        return new Complex(real - toAdd.real, imag - toAdd.imag);
    }

    private Complex kehrwert() {
        double nenner = real*real + imag*imag;
        return new Complex(real/nenner,-imag/nenner);
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
    public Complex sin() {
        return new Complex(cosh(imag) * Math.sin(real), sinh(imag) * Math.cos(real));
    }

    /**
     * Cosine of this Complex number (doesn't change this Complex number).
     * <br>cos(z) = (exp(i*z)+exp(-i*z))/ 2.
     *
     * @return cos(z) where z is this Complex number.
     */
    public Complex cos() {
        return new Complex(cosh(imag) * Math.cos(real), -sinh(imag) * Math.sin(real));
    }

    @Override
    public String toString() {
        return "Complex{" +
                "real=" + real +
                ", imag=" + imag +
                '}';
    }

}
