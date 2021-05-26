package `val`.mx.vbread

import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

class Complex(val real: Double, val imag: Double) {
    fun abs(): Double {
        val decimal = real.pow(2.0) + imag.pow(2.0)

//         double sqrt = Double.longBitsToDouble( ( ( Double.doubleToLongBits( decimal )-(1l<<52) )>>1 ) + ( 1l<<61 ) ); // WHAT https://stackoverflow.com/questions/13263948/fast-sqrt-in-java-at-the-expense-of-accuracy NOTE: NOT FAST
//         return sqrt;
        return sqrt(decimal)
    }

    private fun conjugate(): Complex {
        return Complex(real, -imag)
    }

    fun multiply(complex: Complex): Complex {
        return Complex(
            real * complex.real - imag * complex.imag,
            imag * complex.real + real * complex.imag
        )
    }

    fun multiply(fac: Double): Complex {
        return Complex(real * fac, imag * fac)
    }

    fun divide(div: Complex): Complex {
        return multiply(div.conjugate()).divide(div.imag.pow(2.0) + div.real.pow(2.0))
    }

    fun divide(fac: Double): Complex {
        return Complex(real / fac, imag / fac)
    }

    fun pow(fac: Int): Complex {
        var temp = this
        for (i in 0 until fac) {
            temp = temp.multiply(this)
        }
        return temp
    }

    fun add(real: Double): Complex {
        return Complex(this.real + real, imag)
    }

    fun add(toAdd: Complex): Complex {
        return Complex(real + toAdd.real, imag + toAdd.imag)
    }

    fun subtract(real: Double): Complex {
        return Complex(this.real - real, imag)
    }

    fun subtract(toAdd: Complex): Complex {
        return Complex(real - toAdd.real, imag - toAdd.imag)
    }

    private fun cosh(x: Double): Double {
        return (exp(x) + exp(-x)) / 2
    }

    private fun sinh(x: Double): Double {
        return (exp(x) - exp(-x)) / 2
    }

    fun sin(): Complex {
        return Complex(cosh(imag) * kotlin.math.sin(real), sinh(imag) * kotlin.math.cos(real))
    }

    fun cos(): Complex {
        return Complex(cosh(imag) * kotlin.math.cos(real), -sinh(imag) * kotlin.math.sin(real))
    }

    override fun toString(): String {
        return "Complex{" +
                "real=" + real +
                ", imag=" + imag +
                '}'
    }
}