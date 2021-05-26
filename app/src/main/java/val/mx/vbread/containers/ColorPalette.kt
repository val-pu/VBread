package `val`.mx.vbread.containers

import android.annotation.SuppressLint
import java.util.*

class ColorPalette {
    var baseColor: Int
        private set
    var colors: LinkedList<Int>
        private set
    var title: String
        private set
    private var length: Int

    constructor(title: String, baseColor: Int, vararg colors: Int?) {
        this.baseColor = baseColor
        this.colors = LinkedList(listOf(*colors))
        this.title = title
        length = this.colors.size
    }

    constructor(test: String, i: Int, colors: LinkedList<Int>) {
        title = test
        baseColor = i
        this.colors = colors
        length = this.colors.size
    }

    @SuppressLint("NewApi")
    fun getColorByIteration(iteration: Int): Int {
        if (iteration == -1) return baseColor
        return if (iteration == 0) baseColor else colors[iteration % length]

//        return GetColor(iteration).toArgb();
    }
}