package val.mx.vbread.containers;

import java.util.LinkedList;

public class ColorPalette {

    private LinkedList<Integer> colors;
    private String title;

    public ColorPalette(LinkedList<Integer> colors, String title) {
        this.colors = colors;
        this.title = title;
    }

    public LinkedList<Integer> getColors() {
        return colors;
    }

    public String getTitle() {
        return title;
    }
}
