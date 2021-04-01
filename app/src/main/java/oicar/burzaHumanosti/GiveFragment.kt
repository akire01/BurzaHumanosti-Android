package oicar.burzaHumanosti

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.databinding.FragmentGiveBinding
import oicar.burzaHumanosti.model.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class GiveFragment : Fragment(R.layout.fragment_give) {

    private lateinit var binding: FragmentGiveBinding

    private val pickImage = 100
    private var imageUri: Uri? = null

    private var allCategories: List<Category> = listOf()
    private var allSubCategories: List<SubCategory> = listOf()
    private var allConditions: List<Condition> = listOf()
    private var allDeliveryTypes: List<DeliveryType> = listOf()

    private var selectedSubCategoryId: Int? = null
    private var selectedConditionId: Int? = null
    private var selectedDeliveryTypeId: Int? = null

    private val permissions = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val requestExternalStorage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGiveBinding.inflate(inflater, container, false)

        binding.btnAddPhoto.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        binding.btnAddArticle.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                addArticle()
            }
        }

        askPermission()

        return binding.root
    }

    private fun askPermission() {
        val permission = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissions.toTypedArray(),
                requestExternalStorage
            )
        }
    }

    private suspend fun addArticle() {

        val name = binding.etName.text.toString()
        val description = binding.etDescription.text.toString()
        val checkBox : CheckBox = binding.cbAgree
        val textAgree : TextView = binding.tvAgree

        val result = validateFields(
            name,
            description,
            selectedSubCategoryId,
            selectedConditionId,
            selectedDeliveryTypeId,
            imageUri
        )
        if (!result) {
            return
        }
        else if (!checkBox.isChecked){
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

        val nameBody = RequestBody.create(MediaType.parse("multipart/form-data"), name)
        val descriptionBody =
            RequestBody.create(MediaType.parse("multipart/form-data"), description)
        val subCategoryIdBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            selectedSubCategoryId.toString()
        )
        val conditionIdBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            selectedConditionId.toString()
        )
        val deliveryTypeBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            selectedDeliveryTypeId.toString()
        )

        if (imageUri == null) {
            return
        }

        val file = File(imageUri!!.getRealPath(requireContext())!!)
        val compressedImageFile = Compressor.compress(requireContext(), file)
        val requestFile = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            compressedImageFile
        )
        val imageBody = MultipartBody.Part.createFormData("image", file.name, requestFile)

        RetrofitInstance.getRetrofitInstance().postArticle(
            imageBody, nameBody, descriptionBody,
            subCategoryIdBody, conditionIdBody, deliveryTypeBody
        ).enqueue(object : Callback<Article> {
            override fun onResponse(call: Call<Article>, response: Response<Article>) {
                println(response.body())
                Toast.makeText(context, getString(R.string.successfully_added_text), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Article>, t: Throwable) {
                println(t.message)
            }

        })
    }

    private fun validateFields(
        name: Any, description: Any, selectedSubCategoryId: Int?,
        selectedConditionId: Int?, selectedDeliveryTypeId: Int?, imageUri: Uri?
    ): Boolean {
        if (name.toString().trim().isEmpty() || description.toString().trim().isEmpty()
            || selectedConditionId == null || selectedSubCategoryId == null
            || selectedDeliveryTypeId == null || imageUri == null
        ) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            binding.photo.setImageURI(imageUri)
        }
    }

    override fun onResume() {
        super.onResume()

        getCategories()

        binding.autoCompleteTvCategory.onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String = binding.autoCompleteTvCategory.adapter!!.getItem(
                    position
                ) as String
                getSubCategories(selectedValue)
            }

        binding.autoCompleteTvSubCategory.onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String =
                    binding.autoCompleteTvSubCategory.adapter!!.getItem(position) as String
                val selectedSubCategory = allSubCategories.first { it.name == selectedValue }
                selectedSubCategoryId = selectedSubCategory.id
            }

        binding.autoCompleteTvSubCondition.onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String =
                    binding.autoCompleteTvSubCondition.adapter!!.getItem(position) as String
                val selectedCondition = allConditions.first { it.name == selectedValue }
                selectedConditionId = selectedCondition.id
            }

        binding.autoCompleteTvSubDelivery.onItemClickListener =
            OnItemClickListener { adapterView, view, position, id ->
                val selectedValue: String =
                    binding.autoCompleteTvSubDelivery.adapter!!.getItem(position) as String
                val selectedDeliveryType = allDeliveryTypes.first { it.name == selectedValue }
                selectedDeliveryTypeId = selectedDeliveryType.id
            }

        getConditions()
        getDeliveryTypes()
    }

    private fun getDeliveryTypes() {
        RetrofitInstance.getRetrofitInstance().getDeliveryTypes().enqueue(object :
            retrofit2.Callback<List<DeliveryType>> {
            override fun onResponse(
                call: Call<List<DeliveryType>>,
                response: Response<List<DeliveryType>>
            ) {
                val deliveryTypes: List<DeliveryType>? = response.body()
                val deliveryTypesNames: ArrayList<String> = ArrayList()

                if (deliveryTypes != null) {

                    allDeliveryTypes = deliveryTypes

                    for (item in deliveryTypes) {
                        deliveryTypesNames.add(item.name)
                        val arrayAdapter = ArrayAdapter(
                            requireContext(),
                            R.layout.dropdown_item,
                            deliveryTypesNames
                        )
                        binding.autoCompleteTvSubDelivery.setAdapter(arrayAdapter)
                    }
                }

            }

            override fun onFailure(call: Call<List<DeliveryType>>, t: Throwable) {
                println(t.message)
            }
        })
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
                        val arrayAdapter = ArrayAdapter(
                            requireContext(),
                            R.layout.dropdown_item,
                            conditionNames
                        )
                        binding.autoCompleteTvSubCondition.setAdapter(arrayAdapter)
                    }
                }
            }

            override fun onFailure(call: Call<List<Condition>>, t: Throwable) {
                println(t.message)
            }
        })
    }
}