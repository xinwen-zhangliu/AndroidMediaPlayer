package com.proyecto2_reproductor_de_musica.activities


import android.R.layout.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.data.viewModels.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint
import res.layout.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit  var mediaViewModel : MediaViewModel
    lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        if(!isPermissionGranted()){
            Toast.makeText(this, "You need to accept the permissions to use the application", 1).show()
        }
        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setContentView(binding.root)

         //navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment)!!




//
//        if (navHostFragment != null) {
//            val navController = navHostFragment.navController
//
//            //setupActionBarWithNavController(navHostFragment.navController)
//
//        }




        //var fragment =  ListFragment()

    }




    fun getListFragmentInstance(){
        return
    }


    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }




    @RequiresApi(Build.VERSION_CODES.R)
    fun isPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    //android.Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    //android.Manifest.permission.ACCESS_MEDIA_LOCATION,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,

                ),
                111
            )

            false
        } else {
            true
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        Log.d("x", "on request permission result")
        when (requestCode) {
            111 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("x", "permissions granted")
                    mediaViewModel.getMediaFromFiles(null)
                } else {
                    Log.d("x", "permissions not granted")
                    // Explain to the user that the feature is unavailable because
                    // the feature requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }





}