package oicar.burzaHumanosti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.adapters.StoryViewPagerAdapter
import oicar.burzaHumanosti.databinding.FragmentPositiveBinding
import oicar.burzaHumanosti.model.Help
import oicar.burzaHumanosti.model.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PositiveFragment : Fragment(R.layout.fragment_positive) {

    lateinit var binding: FragmentPositiveBinding
    private lateinit var match: Help
    private var helps : MutableList<Int> = mutableListOf()
    lateinit var text : TextView
    //private lateinit var adapter: MyStoriesRecyclerViewAdapter
    private val storyViewPagerAdapter = StoryViewPagerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPositiveBinding.inflate(inflater, container, false)

        val viewPager = binding.viewPager
        viewPager.adapter = storyViewPagerAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        getAllMatches()
        return binding.root
    }

    private fun getAllMatches() {
        RetrofitInstance.getRetrofitInstance().getAllMatches().enqueue(object: retrofit2.Callback<Help>{
            override fun onResponse(call: Call<Help>, response: Response<Help>) {
                match = response.body()!!
                println(match)
                getHelps(match)
            }

            override fun onFailure(call: Call<Help>, t: Throwable) {
                println(t.message)
            }

        })
    }

    private fun getHelps(match: Help) {
        val giveHelps =  match.giveHelp
        val needHelps = match.needHelp
        for (help in giveHelps){
            helps.add(help.id)
        }
        for (help in needHelps){
            helps.add(help.id)
        }

        helps.forEach {
            getStory(it)
        }

        storyViewPagerAdapter.addStory("Prva jako zanimljiva prica")
        storyViewPagerAdapter.addStory("Hvala na pomoći, Vaš bolnički krevet uvelike je pomogao mojoj obitelji. Sad je njega za našeg oca puno bolja i život mu" +
                "je olakšan za 200%. Najljepša hvala od cijele obitelji Babić!")
        storyViewPagerAdapter.addStory("Hvala što ste nam donirali svoju odjeću. Puno ste nam pomogli jer mi financijski " +
                "nismo u mogućnosti kupovati")
    }

    private fun getStory(helpId: Int){

        RetrofitInstance.getRetrofitInstance().getStory(helpId).enqueue(object: Callback<StoryResponse> {

            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {

                val story = response.body()

                story?.let {
                    val actualStories = story.stories?.map { story -> story.description }

                    actualStories?.let {
                        storyViewPagerAdapter.addStories(it)
                    }

                    binding.indicator.setViewPager(binding.viewPager)
                }
            }
            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                println(t.message)
            }
        })

    }

}