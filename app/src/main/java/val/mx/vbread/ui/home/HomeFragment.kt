package `val`.mx.vbread.ui.home

import `val`.mx.vbread.R
import `val`.mx.vbread.containers.Dimension
import `val`.mx.vbread.views.FractalView
import `val`.mx.vbread.views.JuliaBrotAdapter
import `val`.mx.vbread.views.MandelBrotAdapter
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception
import java.math.BigDecimal
import java.util.*

class HomeFragment : Fragment(), SeekBar.OnSeekBarChangeListener {

    private var currentAdapter: FractalView.Adapter? = null

    // TODO: 01.11.2020 FRAKTALAUSWAHL VERBESSERN
    var fraktal: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    lateinit var fractalView: FractalView
    lateinit var xEditText: EditText
    lateinit var yEditText: EditText
    lateinit var iterEditText: EditText
    lateinit var ausschnittEditText: EditText
    lateinit var btn: Button
    lateinit var btn_fraktal: Button
    lateinit var btn_speichern: Button
    lateinit var seekBar: SeekBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fractalView = view.findViewById(R.id.fractalView)
        btn_speichern = view.findViewById(R.id.button_speichern)
        btn_fraktal = view.findViewById(R.id.button3)
        btn = view.findViewById(R.id.button)
        seekBar = view.findViewById(R.id.seekBar)
        xEditText = view.findViewById(R.id.posXEditText)
        yEditText = view.findViewById(R.id.posYEditText)
        ausschnittEditText = view.findViewById(R.id.posAusschnittEditText)

        btn.setOnClickListener {
            try {
                onResult(
                    BigDecimal(xEditText.text.toString()),
                    BigDecimal(yEditText.text.toString()),
                    BigDecimal(ausschnittEditText.text.toString()),
                    seekBar.progress * 3
                )
            } catch (ex: Exception) {
                Snackbar.make(requireView(), "Fehler bei Eingabe. Bitte wiederholen.", 1)
                ex.printStackTrace()
            }
        }

        button_speichern.setOnClickListener {
            if (fractalView.bitmap != null)
                MediaStore.Images.Media.insertImage(
                    view.context.contentResolver,
                    fractalView.bitmap,
                    UUID.randomUUID().toString(),
                    ""
                )
        }

        btn_fraktal.setOnClickListener {
            Toast.makeText(view.context, "Fraktal geändert!", Toast.LENGTH_LONG).show()
            fraktal = !fraktal
        }

        seekBar.setOnSeekBarChangeListener(this)

        fractalView.homeFragment = this

    }

    companion object private

    fun onResult(
        xs: BigDecimal,
        ys: BigDecimal,
        ye: BigDecimal,
        iteras: Int,
    ) {

        val adapter: FractalView.Adapter = if (fraktal) {
            JuliaBrotAdapter()
        } else {
            MandelBrotAdapter()
        }


        val d = Dimension(
            (xs),
            (xs.subtract(ye)),
            ys,
            (ys.subtract(ye))
        )


        adapter.run {
            setItera(iteras)
            setDimension(
                d
            )
        }
        Log.e("DIMENSION", d.toString())
        currentAdapter = adapter
        fractalView.adapter = adapter

    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

        if (currentAdapter == null) return
        currentAdapter!!.itera = seekBar!!.progress * 3
        fractalView.adapter = currentAdapter!!
    }

    fun updateUI() {
        yEditText.setText(currentAdapter!!.size.down.toDouble().toString())
        xEditText.setText(currentAdapter!!.size.left.toDouble().toString())
    }
}



