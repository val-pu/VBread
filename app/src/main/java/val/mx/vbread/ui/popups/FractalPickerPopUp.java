package val.mx.vbread.ui.popups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

import val.mx.vbread.R;
import val.mx.vbread.adapters.CustomAdapter;
import val.mx.vbread.adapters.FractalPickerAdapter;
import val.mx.vbread.adapters.JuliaBrotAdapter;
import val.mx.vbread.adapters.Magnet1;
import val.mx.vbread.adapters.MandelBrotAdapter;
import val.mx.vbread.adapters.MandelBrotAdapter33;
import val.mx.vbread.adapters.ModdedBrotAdapter;
import val.mx.vbread.adapters.ModifiedBrotAdapter;
import val.mx.vbread.adapters.ParameterizedMandelBrotAdapter;
import val.mx.vbread.adapters.RandomBrotAdapter;
import val.mx.vbread.containers.FractalInfo;
import val.mx.vbread.views.FractalView;

public class FractalPickerPopUp extends DialogFragment implements FractalPickerAdapter.FractalPickListener {

    private FractalView fractalView;

    public FractalPickerPopUp(FractalView fractalView) {
        this.fractalView = fractalView;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fractal_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LinkedList<FractalInfo> dataSet = new LinkedList<>();

        dataSet.add(new FractalInfo("Mandelbrot-Menge", "Z(n+1)=Z(n)^2+c", new MandelBrotAdapter()));
        dataSet.add(new FractalInfo("Parametrized Mandelbrot-Menge", "Z(n+1)=Z(n)^2+c+p", new ParameterizedMandelBrotAdapter()));
        dataSet.add(new FractalInfo("Modified Brot", "Z(n+1)=Z(n)^2+c+p", new ModifiedBrotAdapter()));
        dataSet.add(new FractalInfo("Mandelbrot-Menge ^3", "Z(n+1)=Z(n)^3+c", new MandelBrotAdapter33()));
        dataSet.add(new FractalInfo("Julia-Menge", "Z(n+1)=Z(n)^2+c", new JuliaBrotAdapter()));
        dataSet.add(new FractalInfo("iwas", "?", new CustomAdapter()));
        dataSet.add(new FractalInfo("?", "Z(n+1)=cos(Z(n))+c", new RandomBrotAdapter()));
        dataSet.add(new FractalInfo("ModdedBrot", "Z(n+1)=Z(n)^2+2c", new ModdedBrotAdapter()));

        dataSet.add(new FractalInfo("Magnet1", "tf", new Magnet1()));

        FractalPickerAdapter adapter = new FractalPickerAdapter(this, fractalView, dataSet, requireContext());
        RecyclerView recycler = view.findViewById(R.id.fractal_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);
    }

    @Override
    public void onFractalPick(@NotNull FractalView.Adapter adapter) {
        dismiss();
    }
}
