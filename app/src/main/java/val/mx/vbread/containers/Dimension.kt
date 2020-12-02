package `val`.mx.vbread.containers

import java.math.BigDecimal

class Dimension(
    public val left: BigDecimal,
    public val right: BigDecimal,
    public val top: BigDecimal,
    public val down: BigDecimal
) {

    override fun toString(): String {
        return "$left l $right r $top t $down d"
    }

    fun getDiameter() : BigDecimal {

        return top.subtract(down).abs()
    }


}