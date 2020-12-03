package val.mx.vbread.ui.popups;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.LinkedList;

import val.mx.vbread.R;
import val.mx.vbread.adapters.ColorPickerAdapter;
import val.mx.vbread.containers.ColorPalette;
import val.mx.vbread.views.FractalView;

public class ColorPickerBottomSheet extends BottomSheetDialogFragment {

    private FractalView fractalView;

    public ColorPickerBottomSheet(FractalView fractalView) {
        this.fractalView = fractalView;
    }

    private RecyclerView pickers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_palette_picker, container, false);
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pickers = view.findViewById(R.id.color_recycler);

        pickers.setLayoutManager(new LinearLayoutManager(view.getContext()));

        LinkedList<Integer> colors = new LinkedList<>();
        LinkedList<ColorPalette> palettes = new LinkedList<>();

        colors.add(Color.WHITE);
        colors.add(Color.BLACK);

        palettes.add(new ColorPalette( "Test",Color.BLACK,colors));

        ColorPalette palette = new ColorPalette("Standard", Color.BLACK,
                Color.rgb(9, 1, 47),
                Color.rgb(4, 4, 73),
                Color.rgb(0, 7, 100),
                Color.rgb(12, 44, 138),
                Color.rgb(24, 82, 177),
                Color.rgb(57, 125, 209),
                Color.rgb(134, 181, 229),
                Color.rgb(211, 236, 248),
                Color.rgb(241, 233, 191),
                Color.rgb(248, 201, 95),
                Color.rgb(255, 170, 0),
                Color.rgb(204, 128, 0),
                Color.rgb(153, 87, 0),
                Color.rgb(106, 52, 3)
                //SOURCE https://stackoverflow.com/questions/16500656/which-color-gradient-is-used-to-color-mandelbrot-in-wikipedia
        );

        palettes.add(palette);

        colors = new LinkedList<>();

        for (int i = 0; i < 160; i+=6) {
            colors.add(Color.rgb(i,i,i));
        }

        for (int i = 160; i > 0; i-=6) {
            colors.add(Color.rgb(i,i,i));
        }

        palette = new ColorPalette("BlackNWhite",Color.BLACK,colors);

        palettes.add(palette);

        colors = new LinkedList<>();

        for (int i = 0; i < 160; i+=6) {
            colors.add(Color.rgb(10, (int) (10 + Math.log(i)), (int) (i*1.5)));
        }

        for (int i = 160; i > 0; i-=6) {
            colors.add(Color.rgb(10, (int) (10  + Math.log(i)), (int) (i*1.5)));
        }

        palette = new ColorPalette("wasdas",Color.BLACK,colors);

        palettes.add(palette);


        pickers.setAdapter(new ColorPickerAdapter(view.getContext(), palettes, fractalView,this));


    }

    public interface OnColorPalettePickedListener {

        public void onPalettePick(ColorPalette palette);

    }
}
