package oicar.burzaHumanosti

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.databinding.FragmentLoginBinding
import oicar.burzaHumanosti.model.LoginResponse
import oicar.burzaHumanosti.model.SignInCredentials
import retrofit2.Call
import retrofit2.Response
import java.util.zip.Inflater


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
        (requireActivity() as MainActivity).supportActionBar?.title = "Prijava"

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.clear();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)

        if (isLoggedIn()){
            findNavController().navigate(LoginFragmentDirections.actionGlobalHomeFragment())
        }


        binding.btnSignIn.setOnClickListener {

            var phoneNumber = binding.etUserName.text.toString()
            var password = binding.etPassword.text.toString()

            if (validateCredentials(phoneNumber, password)){
                RetrofitInstance.getRetrofitInstance().signIn(
                    SignInCredentials(
                        phoneNumber,
                        password
                    )
                ).enqueue(object : retrofit2.Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.code() == 201) {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.successfulSignIn),
                                Toast.LENGTH_SHORT
                            ).show()

                            writePreferences(response.body()?.accessToken.toString());

                            findNavController().navigate(LoginFragmentDirections.actionGlobalHomeFragment())

                        } else {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.failedSignIn),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }

    private fun isLoggedIn(): Boolean {
        val prefs: SharedPreferences =
            BurzaHumanostiApp.applicationContext().getSharedPreferences("loginPref",
                Context.MODE_PRIVATE
            )
        val token = prefs.getString("access_token", null)
        return token != null
    }

    private fun writePreferences(token: String) {
        val pref: SharedPreferences =
            requireContext().applicationContext.getSharedPreferences("loginPref", 0)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putString("access_token", token)
        editor.apply()
    }

    private fun validateCredentials(phoneNumber: String, password: String) : Boolean {
        if (phoneNumber.trim().isEmpty() || password.trim().isEmpty()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.failedSignIn))
                .setMessage(getString(R.string.insertPhoneAndPass))
                .setPositiveButton(getString(R.string.txtOK)) { _, _ -> }
                .show()
            return false
        }

        else if(  phoneNumber.length != 13){
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(oicar.burzaHumanosti.R.string.wrongMobile))
                .setMessage(getString(oicar.burzaHumanosti.R.string.mobileLength))
                .setPositiveButton(getString(R.string.txtOK)) { _, _ -> }
                .show()

            binding.etUserName.text.clear()
            binding.etPassword.text.clear()

            return false
        }

        else if(!phoneNumber.startsWith(getString(R.string.mobileStartDigits))){
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(oicar.burzaHumanosti.R.string.wrongMobile))
                .setMessage(getString(oicar.burzaHumanosti.R.string.wrongMobileStartDigits))
                .setPositiveButton(getString(R.string.txtOK)) { _, _ -> }
                .show()

            binding.etUserName.text.clear()
            binding.etPassword.text.clear()

            return false
        }

        return true
    }
}