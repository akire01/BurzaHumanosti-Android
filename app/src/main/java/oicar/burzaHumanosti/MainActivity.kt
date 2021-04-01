package oicar.burzaHumanosti

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import oicar.burzaHumanosti.API.RetrofitInstance
import oicar.burzaHumanosti.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navController = findNavController(R.id.nav_host_fragment_container)
        binding.bottomNavigationView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener{controller, destination, arguments ->
            when(destination.id) {
                R.id.loginFragment -> binding.bottomNavigationView.visibility = View.GONE
                else -> binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.logOut -> {
                logOut()
                true
            }
            R.id.aboutApp ->{
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logOut() {
        val pref: SharedPreferences = this.getSharedPreferences("loginPref", 0)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putString("access_token", null)
        editor.apply()

        navController.navigate(R.id.action_global_loginFragment)
    }

}
