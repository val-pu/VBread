package val.mx.vbread.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import val.mx.vbread.R;
import val.mx.vbread.containers.ColorPalette;
import val.mx.vbread.ui.popups.ColorPickerBottomSheet;
import val.mx.vbread.views.CircularColorDisplayView;
import val.mx.vbread.views.ColorDisplayView;
import val.mx.vbread.views.FractalView;

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    // test
    private LinkedList<ColorPalette> dataSet;
    private FractalView fractalView;
    private ColorPickerBottomSheet parent;

    public ColorPickerAdapter(Context context, LinkedList<ColorPalette> dataSet, FractalView fractalView, ColorPickerBottomSheet parent) {
        inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
        this.fractalView = fractalView;
        this.parent = parent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.palette_picker_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ColorPalette palette = dataSet.get(position);

        holder.itemView.post(() -> {
            holder.colorDisplayView.setData(dataSet.get(position).getColors());
            holder.baseColorDisplay.setData(palette.getBaseColor());
        });

        holder.itemView.setOnClickListener(v -> {
            fractalView.setColorPalette(palette);
            parent.dismiss();
        });

        holder.title.setText(palette.getTitle());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ColorDisplayView colorDisplayView;
        private CircularColorDisplayView baseColorDisplay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            baseColorDisplay = itemView.findViewById(R.id.baseColor);
            colorDisplayView = itemView.findViewById(R.id.colors);
        }
    }
}
