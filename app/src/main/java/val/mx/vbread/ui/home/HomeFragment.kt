package `val`.mx.vbread.ui.home

import `val`.mx.vbread.R
import `val`.mx.vbread.containers.Dimension
import `val`.mx.vbread.ui.popups.ColorPickerBottomSheet
import `val`.mx.vbread.views.FractalView
import `val`.mx.vbread.adapters.MandelBrotAdapter
import `val`.mx.vbread.adapters.RandomBrotAdapter
import `val`.mx.vbread.ui.popups.FractalPickerPopUp
import `val`.mx.vbread.views.FractalView.adapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
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
        editablesInflater = view.findViewById(R.id.value_editor_inflater)

        editablesInflater.setOnClickListener {

            if(editables.visibility == VISIBLE) {
                editablesInflater.setImageDrawable(resources.getDrawable( R.drawable.ic_baseline_arrow_drop_up_24))
                editables.visibility = GONE
            } else {
                editables.visibility = VISIBLE
                editablesInflater.setImageDrawable(resources.getDrawable( R.drawable.ic_baseline_fullscreen_24))
            }
            fractalView.invalidate()
            fractalView.adapter = adapter
        }

        palette_picker.setOnClickListener {

            ColorPickerBottomSheet(fractalView).show(childFragmentManager,"picker")

        }

        btn.setOnClickListener {
            try {
                onResult(
                    BigDecimal(xEditText.text.toString()),
                    BigDecimal(yEditText.text.toString()),
                    BigDecimal(ausschnittEditText.text.toString()),
                    Integer.parseInt(iterations.text.toString())
                )
            } catch (ex: Exception) {
                Snackbar.make(requireView(), "Fehler bei Eingabe. Bitte wiederholen.", 1)
                ex.printStackTrace()
            }
        }

        button_speichern.setOnClickListener {
            if (fractalView.bitmap != null) {

                val imgName = UUID.randomUUID().toString()
                MediaStore.Images.Media.insertImage(
                    view.context.contentResolver,
                    fractalView.bitmap,
                    imgName,
                    ""
                )

                Snackbar.make(view, "Bild als $imgName.png gespeichert!",Snackbar.LENGTH_LONG).show()

            }
        }

        fractal_picker_icon.setOnClickListener {
            FractalPickerPopUp(fractalView).show(childFragmentManager,"ok")
        }



        seekBar.setOnSeekBarChangeListener(this)

        fractalView.homeFragment = this

    }

    companion object Companion {
        private lateinit var fractalView1: FractalView

        fun onResult(
            xs: BigDecimal,
            ys: BigDecimal,
            ye: BigDecimal,
            iteras: Int,
        ) {
            if(adapter == null) return

            val d = Dimension(
                (xs),
                (xs.subtract(ye)),
                ys,
                (ys.subtract(ye))
            )


            adapter!!.run {
                setItera(iteras)
                setDimension(
                    d
                )
            }
            
            fractalView1.adapter = adapter

        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {


        if (adapter == null) return
        adapter!!.itera = seekBar!!.progress * 3
        fractalView.adapter = adapter!!
    }

    fun updateUI() {
        yEditText.setText(adapter!!.dimension.top.toDouble().toString())
        xEditText.setText(adapter!!.dimension.left.toDouble().toString())
        ausschnittEditText.setText(adapter!!.dimension.left.subtract(adapter!!.dimension.right).toString())
    }
}



