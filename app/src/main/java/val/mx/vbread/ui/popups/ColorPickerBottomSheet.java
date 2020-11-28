package val.mx.vbread.ui.popups;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pickers = view.findViewById(R.id.color_recycler);

        pickers.setLayoutManager(new LinearLayoutManager(view.getContext()));

        LinkedList<Integer> colors = new LinkedList<>();
        LinkedList<ColorPalette> palettes = new LinkedList<>();

        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.BLACK);

        palettes.add(new ColorPalette( "Test",0,colors));

        pickers.setAdapter(new ColorPickerAdapter(view.getContext(), palettes, fractalView));


    }

    public interface OnColorPalettePickedListener {

        public void onPalettePick(ColorPalette palette);

    }
}
