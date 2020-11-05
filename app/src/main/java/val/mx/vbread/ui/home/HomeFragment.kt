package `val`.mx.vbread.ui.home

import `val`.mx.vbread.R
import `val`.mx.vbread.containers.Dimension
import `val`.mx.vbread.views.FractalView
import `val`.mx.vbread.views.JuliaBrotAdapter
import `val`.mx.vbread.views.MandelBrotAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.askthing.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception
import java.math.BigDecimal
import java.util.*

class HomeFragment : Fragment(), SeekBar.OnSeekBarChangeListener {


    // TODO: 01.11.2020 FRAKTALAUSWAHL VERBESSERN

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private lateinit var xEditText: EditText
    private lateinit var yEditText: EditText
    lateinit var iterEditText: EditText
    private lateinit var ausschnittEditText: EditText
    private lateinit var btn: Button
    private lateinit var btnFraktal: Button
    private lateinit var btnSpeichern: Button
    private lateinit var seekBar: SeekBar
    private lateinit var editablesInflater: ImageView
    lateinit var editables: ConstraintLayout

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fractalView1 = view.findViewById(R.id.fractalView)
        btnSpeichern = view.findViewById(R.id.button_speichern)
        btnFraktal = view.findViewById(R.id.button3)
        btn = view.findViewById(R.id.button)
        seekBar = view.findViewById(R.id.seekBar)
        xEditText = view.findViewById(R.id.posXEditText)
        yEditText = view.findViewById(R.id.posYEditText)
        ausschnittEditText = view.findViewById(R.id.posAusschnittEditText)
        editables = view.findViewById(R.id.editables)
        editablesInflater = view.findViewById(R.id.editables_inflater)

        editablesInflater.setOnClickListener {

            if(editables.visibility.equals(VISIBLE)) {
                editablesInflater.setImageDrawable(resources.getDrawable( R.drawable.ic_baseline_arrow_drop_up_24))
                editables.visibility = GONE
            } else {
                editables.visibility = VISIBLE
                editablesInflater.setImageDrawable(resources.getDrawable( R.drawable.ic_baseline_fullscreen_24))
            }
            fractalView.invalidate()
            fractalView.adapter = currentAdapter
        }

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

        btnFraktal.setOnClickListener {
            Toast.makeText(view.context, "Fraktal geändert!", Toast.LENGTH_LONG).show()
            fraktal = !fraktal
        }

        seekBar.setOnSeekBarChangeListener(this)

        fractalView.homeFragment = this

    }

    companion object Companion {
        private lateinit var fractalView1: FractalView
        private var currentAdapter: FractalView.Adapter? = null

        private var fraktal: Boolean = false


        public fun onResult(
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
            fractalView1.adapter = adapter

        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

        if (currentAdapter == null) return
        currentAdapter!!.itera = seekBar!!.progress * 3
        fractalView.adapter = currentAdapter!!
    }

    fun updateUI() {
        yEditText.setText(currentAdapter!!.size.top.toDouble().toString())
        xEditText.setText(currentAdapter!!.size.left.toDouble().toString())
    }
}



