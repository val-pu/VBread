package `val`.mx.vbread.adapters

import `val`.mx.vbread.R
import `val`.mx.vbread.containers.FractalInfo
import `val`.mx.vbread.views.FractalView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import java.util.*

class FractalPickerAdapter(val owner: FractalPickListener,val fractalView: FractalView, val dataSet: LinkedList<FractalInfo>, context: Context) : RecyclerView.Adapter<FractalPickerAdapter.VH>() {

    val inflater : LayoutInflater = LayoutInflater.from(context)

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title : TextView = itemView.findViewById(R.id.fractal_name)
        val formula : TextView = itemView.findViewById(R.id.fractal_formula)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(inflater.inflate(R.layout.fractal_picker_card,parent,false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val info = dataSet[position]
        holder.formula.text = info.formula
        holder.title.text = info.name

        holder.itemView.setOnClickListener {
            fractalView.onFractalPick(info.adapter)
            owner.onFractalPick(info.adapter)
            Snackbar.make(holder.itemView,"Successfully picked new Fractal!",Snackbar.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    interface FractalPickListener {
        fun onFractalPick(adapter: FractalView.Adapter)
    }

}