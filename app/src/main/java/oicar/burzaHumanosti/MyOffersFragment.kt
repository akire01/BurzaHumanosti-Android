package oicar.burzaHumanosti

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.adapters.MyOffersRecyclerViewAdapter
import oicar.burzaHumanosti.databinding.FragmentMyOffersBinding
import oicar.burzaHumanosti.model.Help
import oicar.burzaHumanosti.model.Offer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyOffersFragment : Fragment(R.layout.fragment_my_offers) {

    private var offers: List<Offer> = listOf()
    private lateinit var binding: FragmentMyOffersBinding
    private var adapter: MyOffersRecyclerViewAdapter =
        MyOffersRecyclerViewAdapter { offer -> navigateToDetail(offer) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyOffersBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()

        if(!this::binding.isInitialized) {
            return
        }

        getOffers()
    }

    private fun getOffers() {
        RetrofitInstance.getRetrofitInstance().getArticles().enqueue(object :
            Callback<List<Offer>> {
            override fun onResponse(call: Call<List<Offer>>, response: Response<List<Offer>>) {
                offers = response.body()!!
                adapter.setData(offers)
                binding.myOffersRecyclerView.layoutManager = LinearLayoutManager(context)
                binding.myOffersRecyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<List<Offer>>, t: Throwable) {
                println(t.message)
            }

        })
    }

    private fun navigateToDetail(offer: Offer) {
        val action = MyOffersFragmentDirections.actionGlobalOfferDetailFragment(offer)
        findNavController().navigate(action)
    }


}