package `val`.mx.vbread.ui.home

import `val`.mx.vbread.R
import `val`.mx.vbread.containers.Dimension
import `val`.mx.vbread.ui.popups.SettingsSheet
import `val`.mx.vbread.views.FractalView
import `val`.mx.vbread.views.MandelBrotAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.math.BigDecimal

class HomeFragment : Fragment(), SettingsSheet.Result {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//        })
        return root
    }

    lateinit var fractalView: FractalView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fractalView = view.findViewById(R.id.fractalView)

        SettingsSheet(this).show(childFragmentManager,"tag")
    }

    override fun onResult(
        xs: Double?,
        ys: Double?,
        ye: Double?,
        iteras: Int,
        resolution: Int
    ) {

        val adapter = MandelBrotAdapter()
        val d =Dimension(
            BigDecimal(xs!!),
            BigDecimal(xs!! +ye!!),
            BigDecimal(ys!!),
            BigDecimal(ys + ye!!)
        )
        fractalView.setResolution(resolution)
        adapter.run {
            fractalView.setResolution(resolution)
            setMaxItera(iteras)
            setDimension(
                d
            )
        }
        Log.e("DIMENSION",d.toString())
        fractalView.adapter = adapter
    }


}