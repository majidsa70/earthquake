package ir.sadeghi.earthquake.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import ir.sadeghi.earthquake.R
import ir.sadeghi.earthquake.databinding.ActivityMainBinding
import ir.sadeghi.earthquake.ext.buildAlertMessageNoGps
import ir.sadeghi.earthquake.ext.gpsIsOn
import ir.sadeghi.earthquake.ext.isPermittedLocationAccess
import ir.sadeghi.earthquake.ext.showRequestLocationPermission

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


    override fun onResume() {
        super.onResume()

        gpsIsOn().takeIf { it }?.let {
            isPermittedLocationAccess().takeIf { it }?.let {
                Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show()
            } ?: requestLocationPermission()
        } ?: turnOnGps()


    }


    private fun turnOnGps() {
        this.buildAlertMessageNoGps()
    }

    private fun requestLocationPermission() {
        this.showRequestLocationPermission(MY_PERMISSIONS_REQUEST_LOCATION)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show()

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", this.packageName, null),
                            ),
                        )
                    }
                }
                return
            }
        }
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }

}