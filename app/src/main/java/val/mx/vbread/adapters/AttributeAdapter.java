package val.mx.vbread.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.LinkedList;

import val.mx.vbread.R;
import val.mx.vbread.views.FractalView;
import val.mx.vbread.views.ParameterRequestEvent;

public class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.ViewHolder> {

    private final FractalView fractalView;
    private final LayoutInflater inflater;
    private final LinkedList<ParameterRequestEvent.Parameter> parameterData;

    public AttributeAdapter(Context context, FractalView fractalView) {
        inflater = LayoutInflater.from(context);
        this.fractalView = fractalView;

        HashMap<String, ParameterRequestEvent.Parameter> dataSet= FractalView.getParameterRequest().getParameterRequests();

        parameterData = new LinkedList<>(dataSet.values());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.slider_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ParameterRequestEvent.Parameter param = parameterData.get(position);

        holder.paramName.setText(param.getName());

        holder.paramSlider.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        int progress = seekBar.getProgress();
                        double newParam = param.getAbsW() / 100 * progress + param.getMin_value();
                        param.setValue(newParam);
                        fractalView.init();
                    }
                }
        );


    }

    @Override
    public int getItemCount() {
        return parameterData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private SeekBar paramSlider;
        private TextView paramName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            paramName = itemView.findViewById(R.id.param_name);
            paramSlider = itemView.findViewById(R.id.param_slider);
        }
    }
}
