package val.mx.vbread.ui.popups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import val.mx.vbread.R;
import val.mx.vbread.containers.ColorPalette;

public class ColorPickerBottomSheet extends BottomSheetDialogFragment {

    private RecyclerView pickers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.palette_picker_card,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public interface OnColorPalettePickedListener {

        public void onPalettePick(ColorPalette palette);

    }
}
