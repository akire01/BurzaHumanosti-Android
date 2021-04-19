package oicar.burzaHumanosti.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import oicar.burzaHumanosti.R
import oicar.burzaHumanosti.model.*

class MyDemandsRecyclerViewAdapter(val actionDelete : (demand:MyDemand) -> Unit) : RecyclerView.Adapter<MyDemandsRecyclerViewAdapter.ContentViewHolder>() {

    private var data: List<MyDemand> = listOf()

    fun setData(data: List<MyDemand>) {
        this.data = data
        notifyDataSetChanged()
    }


    class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private var category: TextView = itemView.findViewById(R.id.category)
        private var subCategory: TextView = itemView.findViewById(R.id.subCategory)
        private var description: TextView = itemView.findViewById(R.id.description)
        var close : ImageView = itemView.findViewById(R.id.iv_close)

        fun bind(demand: MyDemand){
            category.text = demand.subCategory.category.name
            subCategory.text = demand.subCategory.name
            description.text = "Opis: " + demand.description

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.my_demand,
            parent,
            false
        )
        return ContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val item = data[position]


        holder.bind(item)

        holder.close.setOnClickListener {
            actionDelete(item)
        }

    }

    override fun getItemCount(): Int = data.size
}