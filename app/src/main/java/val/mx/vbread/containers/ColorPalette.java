package val.mx.vbread.containers;

import java.util.Arrays;
import java.util.LinkedList;

public class ColorPalette {

    private int baseColor;
    private LinkedList<Integer> colors;
    private String title;

    public ColorPalette(String title, int baseColor, Integer... colors) {
        this.baseColor = baseColor;
        this.colors = new LinkedList<>(Arrays.asList(colors));
        this.title = title;
    }

    public ColorPalette(String test, int i, LinkedList<Integer> colors) {
        this.title = test;
        this.baseColor = i;
        this.colors = colors;
    }

    public LinkedList<Integer> getColors() {
        return colors;
    }

    public int getBaseColor() {
        return baseColor;
    }

    public String getTitle() {
        return title;
    }
}
