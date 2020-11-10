package val.mx.vbread.containers;

import androidx.annotation.ColorInt;

import java.math.BigDecimal;

public class DrawInfo {
    private final BigDecimal x;
    private final BigDecimal y;
    private final int screenX;
    private final int screenY;
    private int color = -1;

    public DrawInfo(BigDecimal x, BigDecimal y, int screenX, int screenY) {
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
