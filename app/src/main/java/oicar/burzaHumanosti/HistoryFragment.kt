package oicar.burzaHumanosti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import oicar.burzaHumanosti.adapters.ViewPagerAdapter
import oicar.burzaHumanosti.databinding.FragmentHeroesBinding
import oicar.burzaHumanosti.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment(R.layout.fragment_history) {

    private lateinit var binding: FragmentHistoryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHistoryBinding.bind(view)

        val fragmentList = mutableListOf(
            GivenFragment(),
            ReceivedFragment()
        )
        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle)

        binding.actionViewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.actionViewPager){
                tab, position ->
            when(position){
                0->tab.text = getString(R.string.given)
                1->tab.text = getString(R.string.receviced)
                else -> tab.text = getString(R.string.my_history)
            }
        }.attach()

    }

}