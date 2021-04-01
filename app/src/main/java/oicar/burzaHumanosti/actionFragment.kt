package oicar.burzaHumanosti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import oicar.burzaHumanosti.adapters.ActionViewPagerAdapter
import oicar.burzaHumanosti.databinding.FragmentActionBinding


class ActionFragment : Fragment(R.layout.fragment_action) {

    private lateinit var binding: FragmentActionBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentActionBinding.bind(view)
        (requireActivity() as MainActivity).supportActionBar?.title = "Burza Humanosti"

        val fragmentList = arrayListOf<Fragment>(
            GiveFragment(),
            WantFragment()
        )

        val adapter = ActionViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle)

        binding.actionViewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.actionViewPager){
            tab, position ->
            when(position){
                0->tab.text = "Nudim Pomoć"
                1->tab.text = "Tražim pomoć"
                else -> tab.text = "Akcija"
            }
        }.attach()
    }
}