package oicar.burzaHumanosti.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import oicar.burzaHumanosti.R
import oicar.burzaHumanosti.model.GiveHelp
import oicar.burzaHumanosti.model.Offer

class MyOffersRecyclerViewAdapter(val action : (offer: Offer) -> Unit) : RecyclerView.Adapter<MyOffersRecyclerViewAdapter.ContentViewHolder>() {

    private var data: List<Offer> = listOf()
    private var matches: List<GiveHelp> = listOf()

    fun setData(data: List<Offer>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private var photo: ImageView = itemView.findViewById(R.id.ivPhoto)
        private var category: TextView = itemView.findViewById(R.id.category)
        private var subCategory: TextView = itemView.findViewById(R.id.subCategory)
        private var name: TextView = itemView.findViewById(R.id.name)


        fun bind(offer: Offer){

            category.text = offer.subCategory.category.name
            subCategory.text = offer.subCategory.name
            name.text = offer.name

            Glide.with(itemView)
                .load("https://api.bh-app.xyz/article/images/${offer.image}")
                .placeholder(R.drawable.ic_insert_photo)
                .centerCrop()
                .into(photo)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.my_offer,
            parent,
            false
        )
        return ContentViewHolder(view)

    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val item = data[position]



        holder.bind(item)
        holder.itemView.setOnClickListener {
            action(item)
        }

    }
    override fun getItemCount(): Int = data.size
}