package val.mx.vbread.containers;

import android.graphics.Color;

import androidx.annotation.ColorInt;

import java.math.BigDecimal;

import val.mx.vbread.views.FractalView;

public class DrawInfo {
    private final BigDecimal x;
    private final BigDecimal y;
    private final short screenX;
    private final short screenY;
    private int color = 1;

    public DrawInfo(BigDecimal x, BigDecimal y, short screenX, short screenY) {
        this.x = x;
        this.y = y;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    public int getColor() {
        assert color >= 0;
        return color;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }
}
