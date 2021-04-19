package oicar.burzaHumanosti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.databinding.FragmentWantBinding
import oicar.burzaHumanosti.model.Category
import oicar.burzaHumanosti.model.Demand
import oicar.burzaHumanosti.model.SubCategory
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.ArrayList

class WantFragment : Fragment(R.layout.fragment_want) {

    private lateinit var binding: FragmentWantBinding

    private var allCategories: List<Category> = listOf()
    private var allSubCategories: List<SubCategory> = listOf()

    private var selectedSubCategoryId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWantBinding.bind(view)
        binding.btnNeedHelp.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                sendHelpRequest()
            }
        }
    }

    private fun sendHelpRequest() {
        val description = binding.etDescription.text.toString()
        val checkBox: CheckBox = binding.cbAgree
        val textAgree: TextView = binding.tvAgree

        val result = validateFields(
            description,
            selectedSubCategoryId
        )
        if (!result) {
            return
        } else if (!checkBox.isChecked) {
            textAgree.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorRed))
            requireActivity().runOnUiThread {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.terms_of_use_title))
                    .setMessage(getString(R.string.terms_of_use_message))
                    .setPositiveButton(getString(R.string.txtOK)) { _, _ -> }
                    .show()
            }
            return
        }
        
        RetrofitInstance.getRetrofitInstance()
            .postDemand(Demand(description, selectedSubCategoryId!!))
            .enqueue(object : Callback<Demand> {
                override fun onResponse(call: Call<Demand>, response: Response<Demand>) {
                    println(response.body())
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.demand_request))
                        .setMessage(getString(R.string.demand_request_success))
                        .setPositiveButton(getString(R.string.txtOK)) { _, _ -> }
                        .show()

                    binding.autoCompleteTvCategory.setText("",false)
                    binding.autoCompleteTvSubCategory.setText("",false)
                    binding.etDescription.text.clear()
                    binding.cbAgree.isChecked = false
                }

                override fun onFailure(call: Call<Demand>, t: Throwable) {
                    println(t.message)
                }
            })

    }

    private fun validateFields(description: String, selectedSubCategoryId: Int?): Boolean {
        if (description.trim().isEmpty() || selectedSubCategoryId == null) {
            requireActivity().runOnUiThread {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.form))
                    .setMessage(getString(R.string.fill_all_fields_message))
                    .setPositiveButton(getString(R.string.txtOK)) { _, _ -> }
                    .show()
            }
            return false
        }
        return true
    }


    override fun onResume() {
        super.onResume()
        getCategories()

        binding.autoCompleteTvCategory.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedValue: String = binding.autoCompleteTvCategory.adapter!!.getItem(
                    position
                ) as String
                getSubCategories(selectedValue)
            }

        binding.autoCompleteTvSubCategory.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedValue: String = binding.autoCompleteTvSubCategory.adapter!!.getItem(
                    position
                ) as String
                val selectedSubcategory = allSubCategories.first { it.name == selectedValue }
                selectedSubCategoryId = selectedSubcategory.id
                println(selectedSubCategoryId)
            }
    }

    private fun getSubCategories(selectedValue: String) {
        val subCategoryNames: ArrayList<String> = ArrayList()

        for (item in allCategories) {
            allSubCategories = allCategories.flatMap { it.subCategories }
            if (item.name == selectedValue) {
                for (it in item.subCategories) {
                    subCategoryNames.add(it.name)
                }
                val arrayAdapter = ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_item,
                    subCategoryNames
                )
                binding.autoCompleteTvSubCategory.setAdapter(arrayAdapter)
            }
        }
    }

    private fun getCategories() {
        RetrofitInstance.getRetrofitInstance().getCategories()
            .enqueue(object : retrofit2.Callback<List<Category>> {
                override fun onResponse(
                    call: Call<List<Category>>,
                    response: Response<List<Category>>
                ) {
                    val categories: List<Category>? = response.body()
                    val categoryNames: ArrayList<String> = ArrayList()

                    if (categories != null) {
                        allCategories = categories
                        for (item in categories) {
                            categoryNames.add(item.name)
                            val arrayAdapter = ArrayAdapter(
                                requireContext(),
                                R.layout.dropdown_item,
                                categoryNames
                            )
                            binding.autoCompleteTvCategory.setAdapter(arrayAdapter)
                        }
                    }
                }

                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    println(t.message)
                }

            })
    }

}