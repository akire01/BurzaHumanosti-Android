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
import oicar.burzaHumanosti.model.ConfirmLoginResponse
import oicar.burzaHumanosti.model.LoginResponse
import oicar.burzaHumanosti.model.SignInCredentials
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


 class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

     private lateinit var pref: SharedPreferences

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

        pref =  requireContext().applicationContext.getSharedPreferences("loginPref", 0)

        binding = FragmentLoginBinding.bind(view)

        if (isLoggedIn()){
            val phoneNumber = pref.getString("phoneNumber", "")
            val password =pref.getString("password", "")
            login(phoneNumber.toString(), password.toString())
            //sendToConfirmationToServer()
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

                            writePreferences(response.body()?.accessToken.toString(), phoneNumber, password)

                            sendToConfirmationToServer()

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

    private fun sendToConfirmationToServer() {
        RetrofitInstance.getRetrofitInstance().confirmLogin().enqueue(object :
            Callback<ConfirmLoginResponse> {
            override fun onResponse(
                call: Call<ConfirmLoginResponse>,
                response: Response<ConfirmLoginResponse>
            ) {
                println("---------------> " + response.body()?.message)
                findNavController().navigate(LoginFragmentDirections.actionGlobalHomeFragment())
            }

            override fun onFailure(call: Call<ConfirmLoginResponse>, t: Throwable) {
                println("---------------> " + t.message)
            }

        })
    }

    private fun isLoggedIn(): Boolean {
        val prefs: SharedPreferences =
            BurzaHumanostiApp.applicationContext().getSharedPreferences(
                "loginPref",
                Context.MODE_PRIVATE
            )
        val token = prefs.getString("access_token", null)
        return token != null
    }

    private fun writePreferences(token: String, phoneNumber: String, password: String) {

        val editor: SharedPreferences.Editor = pref.edit()
        editor.putString("access_token", token)
        editor.putString("phoneNumber", phoneNumber)
        editor.putString("password", password)
        editor.apply()
    }

    private fun validateCredentials(phoneNumber: String, password: String) : Boolean {
        if (phoneNumber.trim().isEmpty() || password.trim().isEmpty()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.empty_credentials))
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

    private fun login(phoneNumber: String, password: String) {
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

                    writePreferences(response.body()?.accessToken.toString(), phoneNumber, password)
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