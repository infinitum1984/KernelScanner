package com.kernel.scanner.cargo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
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

        binding = ActivityCargoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_cargo)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_cargo)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}