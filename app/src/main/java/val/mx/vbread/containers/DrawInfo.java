package val.mx.vbread.containers;

import androidx.annotation.ColorInt;

public class DrawInfo {
    private final double x;
    private final double y;
    private final int screenX;
    private final int screenY;
    private int color = -100;

    public DrawInfo(double x, double y, int screenX, int screenY) {
        this.x = x;
        this.y = y;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    @Override
    public String toString() {
        return "DrawInfo{" +
                "x=" + x +
                ", y=" + y +
                ", screenX=" + screenX +
                ", screenY=" + screenY +
                ", color=" + color +
                '}';
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
