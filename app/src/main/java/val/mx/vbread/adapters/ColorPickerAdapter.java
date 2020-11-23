package val.mx.vbread.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import val.mx.vbread.R;
import val.mx.vbread.containers.ColorPalette;
import val.mx.vbread.views.ColorDisplayView;
import val.mx.vbread.views.FractalView;

public class ColorPickerAdapter extends RecyclerView.Adapter<ColorPickerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private LinkedList<ColorPalette> dataSet;
    private FractalView fractalView;

    public ColorPickerAdapter(Context context, LinkedList<ColorPalette> dataSet, FractalView fractalView) {
        inflater = LayoutInflater.from(context);
        this.dataSet = dataSet;
        this.fractalView = fractalView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.palette_picker_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.post(()-> {
            holder.colorDisplayView.setData(dataSet.get(position).getColors());
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ColorDisplayView colorDisplayView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            colorDisplayView = itemView.findViewById(R.id.colors);
        }
    }
}
