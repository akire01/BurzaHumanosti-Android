package oicar.burzaHumanosti

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import oicar.burzaHumanosti.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private  lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment).navController
        binding.bottomNavigationView.setupWithNavController(navController)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.loginFragment,
            R.id.homeFragment,
            R.id.actionFragment,
            R.id.positiveFragment,
            R.id.heroesFragment,
            R.id.historyFragment
        ))

        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener{ controller, destination, arguments ->
            when(destination.id) {
                R.id.loginFragment,
                R.id.offerDetailFragment -> binding.bottomNavigationView.visibility = View.GONE
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
            R.id.aboutApp -> {
                navController.navigate(R.id.action_global_aboutAppFragment)
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

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }


}
