package oicar.burzaHumanosti

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.adapters.MyDemandsRecyclerViewAdapter
import oicar.burzaHumanosti.databinding.FragmentMyDemandsBinding
import oicar.burzaHumanosti.model.DeletionResponse
import oicar.burzaHumanosti.model.Help
import oicar.burzaHumanosti.model.MyDemand
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyDemandsFragment : Fragment(R.layout.fragment_my_demands) {

    private var demands: List<MyDemand> = listOf()
    private lateinit var binding : FragmentMyDemandsBinding
    private var adapter: MyDemandsRecyclerViewAdapter = MyDemandsRecyclerViewAdapter{ demand -> showDialog(
        demand
    ) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyDemandsBinding.bind(view)

        getDemands()
    }


    private fun getDemands() {
        RetrofitInstance.getRetrofitInstance().getMyDemands().enqueue(object : Callback<List<MyDemand>> {
            override fun onResponse(
                call: Call<List<MyDemand>>,
                response: Response<List<MyDemand>>
            ) {
                demands = response.body()!!
                adapter.setData(demands)
                binding.myDemandsRecyclerView.layoutManager = LinearLayoutManager(context)
                binding.myDemandsRecyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<List<MyDemand>>, t: Throwable) {
                println(t.message)
            }

        })
    }
    private fun showDialog(demand: MyDemand) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.deleting))
        builder.setMessage(getString(R.string.sure_to_delete))

        builder.setPositiveButton(
           getString(R.string.yes),
            DialogInterface.OnClickListener { dialog, which ->
                deleteDemand(demand)
                dialog.dismiss()
            })

        builder.setNegativeButton(
            getString(R.string.no),
            DialogInterface.OnClickListener { dialog, which -> // Do nothing
                dialog.dismiss()
            })

        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun deleteDemand(demand: MyDemand) {
        RetrofitInstance.getRetrofitInstance().deleteDemand(demand.id).enqueue(object : Callback<DeletionResponse>{
            override fun onResponse(
                call: Call<DeletionResponse>,
                response: Response<DeletionResponse>
            ) {
                Toast.makeText(requireContext(), getString(R.string.success_deleted), Toast.LENGTH_SHORT).show()
                getDemands()
            }

            override fun onFailure(call: Call<DeletionResponse>, t: Throwable) {
                println(t.message)
            }


        })
    }


}