package val.mx.vbread.ui.popups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import val.mx.vbread.R;

public class SettingsSheet extends DialogFragment {

    private Result act;

    public SettingsSheet(Result act) {

        this.act = act;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.askthing, container, false);
    }

    private EditText xs, xe, ys, auGroesse, resolution, iterationen;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        xs = view.findViewById(R.id.xs);
        ys = view.findViewById(R.id.ys);
        auGroesse = view.findViewById(R.id.auGroesse);
        resolution = view.findViewById(R.id.genauigkeit);
        iterationen = view.findViewById(R.id.iterationen);

        view.findViewById(R.id.done).setOnClickListener(v -> {

            act.onResult(Double.parseDouble(xs.getText().toString()), Double.parseDouble(ys.getText().toString()), Double.parseDouble(auGroesse.getText().toString()), Integer.parseInt(iterationen.getText().toString()), Integer.parseInt(resolution.getText().toString()));

        });

    }

    public interface Result {
        public void onResult(Double xs, Double ys, Double auGroesse, int iteras, int resolution);
    }
}
