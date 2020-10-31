package `val`.mx.vbread.containers

import java.math.BigDecimal

class Dimension(
    val left: BigDecimal,
    val right: BigDecimal,
    val top: BigDecimal,
    val down: BigDecimal
) {

    override fun toString(): String {
        return "$left l $right r $top t $down d"
    }
}