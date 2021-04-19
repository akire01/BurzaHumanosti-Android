package oicar.burzaHumanosti.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import oicar.burzaHumanosti.MyDemandsFragment
import oicar.burzaHumanosti.MyOffersFragment

class ViewPagerAdapter(
    list: MutableList<Fragment>,
    fm: FragmentManager,
    lifecycle: Lifecycle ): FragmentStateAdapter(fm, lifecycle) {

    private val fragmentList : MutableList<Fragment> = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return  fragmentList[position]
    }
}