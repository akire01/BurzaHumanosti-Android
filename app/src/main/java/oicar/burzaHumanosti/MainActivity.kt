package oicar.burzaHumanosti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.databinding.ActivityMainBinding
import oicar.burzaHumanosti.model.SignInCredentials
import retrofit2.Call
import retrofit2.Response
import oicar.burzaHumanosti.model.LoginResponse

class MainActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnSignIn.setOnClickListener {

            var phoneNumber = binding.etUserName.text.toString()
            var password = binding.etPassword.text.toString()

            if (validateCredentials(phoneNumber, password)){
                RetrofitInstance.getRetrofitInstance().signIn(SignInCredentials(phoneNumber, password)).enqueue(object: retrofit2.Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.code() == 201) {
                            Toast.makeText(this@MainActivity, getString(R.string.successfulSignIn), Toast.LENGTH_SHORT)
                                .show()
                        } else{
                            Toast.makeText(this@MainActivity, getString(R.string.failedSignIn), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(
                            this@MainActivity,
                            t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
         }
    }


    private fun validateCredentials(phoneNumber: String, password: String) : Boolean {
        if (phoneNumber.trim().isEmpty() || password.trim().isEmpty()) {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.failedSignIn))
                .setMessage(getString(R.string.insertPhoneAndPass))
                .setPositiveButton(getString(R.string.txtOK)) { _, _ -> }
                .show()
            return false
        }

        else if(  phoneNumber.length != 13){
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(oicar.burzaHumanosti.R.string.wrongMobile))
                .setMessage(getString(oicar.burzaHumanosti.R.string.mobileLength))
                .setPositiveButton(getString(R.string.txtOK)) { _, _ -> }
                .show()

            binding.etUserName.text.clear()
            binding.etPassword.text.clear()

            return false
        }

        else if(!phoneNumber.startsWith(getString(R.string.mobileStartDigits))){
            MaterialAlertDialogBuilder(this)
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
