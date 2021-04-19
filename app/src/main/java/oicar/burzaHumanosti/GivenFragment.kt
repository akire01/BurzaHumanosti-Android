package oicar.burzaHumanosti

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.adapters.MyHistoryRecyclerViewAdapter
import oicar.burzaHumanosti.databinding.FragmentGivenBinding
import oicar.burzaHumanosti.model.HelpResponse
import retrofit2.Call
import retrofit2.Response

class GivenFragment: Fragment(R.layout.fragment_given) {

    private lateinit var binding: FragmentGivenBinding
    private lateinit var adapter: MyHistoryRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGivenBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()

        if(!this::binding.isInitialized) {
            return
        }

        getHelps()
    }

    private fun getHelps() {
        RetrofitInstance.getRetrofitInstance().getGiven().enqueue(object: retrofit2.Callback<List<HelpResponse>>{
            override fun onResponse(
                call: Call<List<HelpResponse>>,
                response: Response<List<HelpResponse>>
            ) {
               println(response.body())
                adapter= MyHistoryRecyclerViewAdapter(response.body()!!)
                binding.myHelpsRecyclerView.layoutManager = LinearLayoutManager(context)
                binding.myHelpsRecyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<List<HelpResponse>>, t: Throwable) {
                println(t.message)
            }

        })
    }

}