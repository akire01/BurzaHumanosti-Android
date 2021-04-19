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
import oicar.burzaHumanosti.model.HelpResponse

class MyHistoryRecyclerViewAdapter(val helps: List<HelpResponse>) :
    RecyclerView.Adapter<MyHistoryRecyclerViewAdapter.ContentViewHolder>(){


    class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private var name: TextView = itemView.findViewById(R.id.helpName)
        private var condition: TextView = itemView.findViewById(R.id.condition)
        private var status: TextView = itemView.findViewById(R.id.status)
        private var person: TextView = itemView.findViewById(R.id.person)
        private var image : ImageView = itemView.findViewById(R.id.ivPhoto)

        fun bind(help: HelpResponse){
            name.text = help.article.name
            condition.text = "Stanje: " + help.article.articleCondition?.name

            status.text = help.article.delivery.deliveryStatus.name
            when(help.article.delivery.deliveryStatus.id){
                2 -> status.setTextColor(Color.parseColor("#DC4067"))
                3->status.setTextColor(Color.parseColor("#2196F3"))
                4->status.setTextColor(Color.parseColor("#40C891"))
            }

            print(help)
            if (help.account?.firstName != null || help.account?.lastName != null){
                person.text =  "Potrebita osoba: " + help.account?.firstName + " " + help.account?.lastName
            }
            else if(help.article.account?.firstName != null || help.article.account?.lastName != null){
                person.text = "Donator: " + help.article.account?.firstName + " " + help.article.account?.lastName
            }
            Glide.with(itemView)
                .load("https://api.bh-app.xyz/article/images/${help.article.image}")
                .placeholder(R.drawable.ic_insert_photo)
                .centerCrop()
                .into(image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.history_item,
            parent,
            false
        )
        return ContentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        val item = helps[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = helps.size

}