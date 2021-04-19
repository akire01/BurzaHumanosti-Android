package oicar.burzaHumanosti

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.databinding.FragmentOfferDetailBinding
import oicar.burzaHumanosti.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OfferDetailFragment : Fragment(R.layout.fragment_offer_detail) {

    private lateinit var binding: FragmentOfferDetailBinding
    private val args: OfferDetailFragmentArgs by navArgs()
    private lateinit var offer: Offer

    private val pickImage = 100
    private var imageUri: Uri? = null

    private var allCategories: List<Category> = listOf()
    private var allSubCategories: List<SubCategory> = listOf()
    private var allConditions: List<Condition> = listOf()
    private var selectedSubCategoryId: Int = 0
    private lateinit var selectedCondition: Condition

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOfferDetailBinding.inflate(inflater, container, false)

        offer = args.offer!!

        fillFields()

        binding.btnDelete.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.deleting))
                .setMessage(R.string.sure_to_delete)
                .setNegativeButton(getString(R.string.no)) { dialog, which ->
                }
                .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                    RetrofitInstance.getRetrofitInstance().deleteArticle(offer.id)
                        .enqueue(object :
                            Callback<DeletionResponse> {
                            override fun onResponse(
                                call: Call<DeletionResponse>,
                                response: Response<DeletionResponse>
                            ) {

                                MaterialAlertDialogBuilder(requireContext())
                                    .setTitle(getString(R.string.success))
                                    .setMessage(R.string.success_deleted)
                                    .setPositiveButton(getString(R.string.txtOK)) { _, _ -> findNavController().popBackStack() }
                                    .show()

                                println(response.message())
                            }

                            override fun onFailure(call: Call<DeletionResponse>, t: Throwable) {
                                println(t.message)
                            }
                        })
                }
                .show()
        }

        binding.btnUpdate.setOnClickListener {
            RetrofitInstance.getRetrofitInstance().updateArticle(
                offer.id,
                UpdateArticle(
                    binding.etName.text.toString(),
                    binding.etDescription.text.toString(),
                    selectedSubCategoryId,
                    selectedCondition.id
                )
            ).enqueue(object : Callback<Article> {
                override fun onResponse(
                    call: Call<Article>,
                    response: Response<Article>
                ) {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(R.string.success)
                        .setMessage(getString(R.string.success_updated))
                        .setPositiveButton(getString(R.string.txtOK)) { _, _ -> }
                        .show()
                }

                override fun onFailure(call: Call<Article>, t: Throwable) {
                    println(t.message)
                }
            })
        }

        return binding.root
    }


    override fun onResume() {
        super.onResume()

        getCategories()
        getConditions()

        binding.autoCompleteTvCategory.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String = binding.autoCompleteTvCategory.adapter!!.getItem(
                    position
                ) as String
                getSubCategories(selectedValue)
            }

        binding.autoCompleteTvSubCategory.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String = binding.autoCompleteTvSubCategory.adapter!!.getItem(
                    position
                ) as String
                val value = allSubCategories.first { it.name == selectedValue }
                selectedSubCategoryId = value.id
            }
        binding.autoCompleteTvCondition.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String = binding.autoCompleteTvCondition.adapter!!.getItem(
                    position
                ) as String
                selectedCondition = allConditions.first { it.name == selectedValue }
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

    private fun fillFields() {

        binding.autoCompleteTvCategory.setText(
            offer.subCategory.category.name,
            false
        )

        binding.autoCompleteTvSubCategory.setText(
            offer.subCategory.name,
            false
        )

        binding.autoCompleteTvCondition.setText(
            offer.articleCondition.name,
            false
        )

        binding.etName.setText(offer.name)
        binding.etDescription.setText(offer.description)

        if (offer.image != null) {

            Glide.with(this)
                .load("https://api.bh-app.xyz/article/images/${offer.image}")
                .centerCrop()
                .into(binding.ivPhoto)
        }

        selectedSubCategoryId = offer.subCategory.id
        selectedCondition = offer.articleCondition

    }

    private fun getCategories() {
        RetrofitInstance.getRetrofitInstance().getCategories().enqueue(object :
            retrofit2.Callback<List<Category>> {
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
                    }

                    val arrayAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.dropdown_item,
                        categoryNames
                    )

                    binding.autoCompleteTvCategory.setAdapter(arrayAdapter)
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun getConditions() {
        RetrofitInstance.getRetrofitInstance().getConditions().enqueue(object :
            retrofit2.Callback<List<Condition>> {
            override fun onResponse(
                call: Call<List<Condition>>,
                response: Response<List<Condition>>
            ) {
                val conditions: List<Condition>? = response!!.body()
                val conditionNames: ArrayList<String> = ArrayList()

                if (conditions != null) {
                    allConditions = conditions

                    for (item in conditions) {
                        conditionNames.add(item.name)
                    }

                    val arrayAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.dropdown_item,
                        conditionNames
                    )
                    binding.autoCompleteTvCondition.setAdapter(arrayAdapter)
                }
            }

            override fun onFailure(call: Call<List<Condition>>, t: Throwable) {
                println(t.message)
            }
        })
    }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.ivPhoto.setImageURI(imageUri)
        }
    }

}