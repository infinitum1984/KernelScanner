package com.kernel.scanner.cargo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kernel.scanner.R
import com.kernel.scanner.databinding.ActivityCargoBinding

class CargoActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCargoBinding
    companion object{
        fun startActivity(context: Context, cargoId:Long){
            val intent= Intent(context,CargoActivity::class.java)
            intent.putExtra("CARGO_ID",cargoId)
            context.startActivity(intent)
        }
        fun getIdFromIntent(intent: Intent):Long{
            return intent.getLongExtra("CARGO_ID",0);
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.analytics
        binding = ActivityCargoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_cargo)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cargo_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_ok->{
                finish()
                return true
            }
            android.R.id.home->{
                val navController = findNavController(R.id.nav_host_fragment_content_cargo)
                if (navController.currentBackStackEntry!!.destination.id==R.id.navigation_scanner){
                    navController.navigate(R.id.action_navigation_scanner_to_navigation_cargo)
                }else{
                    finish()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.nav_host_fragment_content_cargo)
        if (navController.currentBackStackEntry!!.destination.id==R.id.navigation_scanner){
            navController.navigate(R.id.action_navigation_scanner_to_navigation_cargo)
        }else{
            finish()
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_cargo)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}