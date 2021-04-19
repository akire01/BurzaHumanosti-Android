package oicar.burzaHumanosti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.adapters.ViewPagerAdapter
import oicar.burzaHumanosti.databinding.FragmentHomeBinding
import oicar.burzaHumanosti.model.ConfirmLoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val fragmentList = mutableListOf(
        MyOffersFragment(),
        MyDemandsFragment()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        setupPager()

    }


    override fun onResume() {
        super.onResume()
        fragmentList[0].onResume()
        fragmentList[1].onResume()
    }

    private fun setupPager() {
        viewPagerAdapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle)

        binding.actionViewPager.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.actionViewPager){
                tab, position ->
            when(position){
                0->tab.text = getString(R.string.my_offers)
                1->tab.text = getString(R.string.my_searchings)
                else -> tab.text = getString(R.string.home)
            }
        }.attach()

    }



}