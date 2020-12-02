package val.mx.vbread.containers;

import android.annotation.SuppressLint;
import android.graphics.Color;

import java.util.Arrays;
import java.util.LinkedList;

public class ColorPalette {

    private int baseColor;
    private LinkedList<Integer> colors;
    private String title;
    private int length;

    public ColorPalette(String title, int baseColor, Integer... colors) {
        this.baseColor = baseColor;
        this.colors = new LinkedList<>(Arrays.asList(colors));
        this.title = title;
        length = this.colors.size();
    }

    public ColorPalette(String test, int i, LinkedList<Integer> colors) {
        this.title = test;
        this.baseColor = i;
        this.colors = colors;
    }

    public LinkedList<Integer> getColors() {
        return colors;
    }

    @SuppressLint("NewApi")
    public int getColorByIteration(int iteration) {

        if(iteration == -1) return baseColor;

        if(iteration == 0) return baseColor;

        return GetColor(iteration).toArgb();
//        return colors.get(iteration%length);
    }


    // Get a color for this pixel.
    @SuppressLint("NewApi")
    private Color GetColor(double mu)
    {
        int clr1 = (int)mu;
        double t2 = mu - clr1;
        double t1 = 1 - t2;
        clr1 = clr1 % colors.size();
        int clr2 = (clr1 + 1) % colors.size();

        byte r = (byte)(colors.get(clr1) * t1 + colors.get(clr2) * t2);

        return Color.valueOf(255, r, 0, 0);
    }

    public int getBaseColor() {
        return baseColor;
    }

    public String getTitle() {
        return title;
    }
}
