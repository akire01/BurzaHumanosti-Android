package oicar.burzaHumanosti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import oicar.burzaHumanosti.adapters.ViewPagerAdapter
import oicar.burzaHumanosti.databinding.FragmentActionBinding


class ActionFragment : Fragment(R.layout.fragment_action) {

    private lateinit var binding: FragmentActionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentActionBinding.bind(view)

        val fragmentList = mutableListOf(
            GiveFragment(),
            WantFragment()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle)

        binding.actionViewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.actionViewPager){
            tab, position ->
            when(position){
                0->tab.text = getString(R.string.giving_help)
                1->tab.text = getString(R.string.searching_for_a_help)
                else -> tab.text = getString(R.string.action)
            }
        }.attach()
    }
}