package oicar.burzaHumanosti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.adapters.HeroViewPagerAdapter
import oicar.burzaHumanosti.databinding.FragmentHeroesBinding
import oicar.burzaHumanosti.model.Hero
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HeroesFragment : Fragment(R.layout.fragment_heroes) {

    private lateinit var binding: FragmentHeroesBinding

    private var heroes : List<Hero> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeroesBinding.inflate(inflater,container, false)
        getAllHeroes()
        return binding.root
    }

    private fun getAllHeroes() {
       RetrofitInstance.getRetrofitInstance().getHeroes().enqueue(object : Callback<List<Hero>>{
           override fun onResponse(call: Call<List<Hero>>, response: Response<List<Hero>>) {
               heroes = response.body()!!
               println(heroes)
               val viewPager = binding.viewPagerHeroes
               val adapter = HeroViewPagerAdapter(heroes)
               viewPager.adapter = adapter
               viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
               binding.indicatorHeroes.setViewPager(viewPager)
           }

           override fun onFailure(call: Call<List<Hero>>, t: Throwable) {
              println(t.message)
           }

       })
    }
}

