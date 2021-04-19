package oicar.burzaHumanosti.adapters


import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import oicar.burzaHumanosti.R
import oicar.burzaHumanosti.model.Hero
import kotlin.random.Random


class HeroViewPagerAdapter(var heroes: List<Hero> = listOf()) : RecyclerView.Adapter<HeroViewPagerAdapter.Pager2ViewHolder>() {

    class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var stars1: ImageView = itemView.findViewById(R.id.iv1)
        private var stars2: ImageView = itemView.findViewById(R.id.iv2)
        private var stars3: ImageView = itemView.findViewById(R.id.iv3)
        private var stars4: ImageView = itemView.findViewById(R.id.iv4)
        private var person: ImageView = itemView.findViewById(R.id.iv_person)
        private var helps: TextView = itemView.findViewById(R.id.tv_numberOfHelps)
        private var fullName: TextView = itemView.findViewById(R.id.tv_fullName)
        private var organisation: TextView = itemView.findViewById(R.id.tv_organisation)

        fun bind(hero: Hero){

            helps.text = "Ukupan broj donacija: " + hero.helped.toString()
            fullName.text = hero.firstName + " " + hero.lastName
            if (hero.organization != ""){
                organisation.text = "Organizacija: " + hero.organization
            }

            Glide.with(itemView)
                .load("https://api.bh-app.xyz/account/avatar/${hero.image}")
                .placeholder(R.drawable.ic_insert_photo)
                .circleCrop()
                .into(person)

            val rnd = Random
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            ImageViewCompat.setImageTintList(stars1, ColorStateList.valueOf(color))
            ImageViewCompat.setImageTintList(stars2, ColorStateList.valueOf(color))
            ImageViewCompat.setImageTintList(stars3, ColorStateList.valueOf(color))
            ImageViewCompat.setImageTintList(stars4, ColorStateList.valueOf(color))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.hero_item,
            parent,
            false
        )
        return Pager2ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        val item = heroes[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = heroes.size


}