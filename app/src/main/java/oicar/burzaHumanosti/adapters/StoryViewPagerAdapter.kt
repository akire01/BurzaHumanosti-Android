package oicar.burzaHumanosti.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.scaleMatrix
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import oicar.burzaHumanosti.R
import kotlin.random.Random

class StoryViewPagerAdapter() : RecyclerView.Adapter<StoryViewPagerAdapter.Pager2ViewHolder>() {

    class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var story: TextView = itemView.findViewById(R.id.tvStory)
        private var heart: ImageView = itemView.findViewById(R.id.ivHeart)

        fun bind(text: String){
            story.text = text

            val rnd = Random
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            ImageViewCompat.setImageTintList(heart, ColorStateList.valueOf(color))

            heart.animate().apply {
                duration=1000
                scaleX(4f)
            }.scaleY(4f).withEndAction {
                heart.animate().apply {
                    duration=1000
                    scaleX(2.5f)
                }.scaleY(2.5f)
            }
        }
    }

    var stories: MutableList<String> = mutableListOf()

    fun addStories(stories: List<String>) {
        this.stories.addAll(stories)
        notifyDataSetChanged()
    }

    fun addStory(story: String) {
        stories.add(story)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Pager2ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.my_story,
            parent,
            false
        )
        return Pager2ViewHolder(view)
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
       val item = stories[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int = stories.size
}