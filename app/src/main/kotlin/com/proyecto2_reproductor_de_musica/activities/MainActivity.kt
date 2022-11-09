package com.proyecto2_reproductor_de_musica.activities


import android.R.layout.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.proyecto2_reproductor_de_musica.R
import com.proyecto2_reproductor_de_musica.data.viewModels.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint
import res.layout.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit  var mediaViewModel : MediaViewModel

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        if(!isPermissionGranted()){
            Toast.makeText(this, "You need to accept the permissions to use the application", 1).show()
        }
        mediaViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)

         mediaViewModel
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment?
//
//        if (navHostFragment != null) {
//            val navController = navHostFragment.navController
//
//            //setupActionBarWithNavController(navHostFragment.navController)
//
//        }


        //var fragment =  ListFragment()

    }




    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
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




//    @RequiresApi(Build.VERSION_CODES.P)
//    fun getLabels(){
//        val mmr = MediaMetadataRetriever()
//
//
//        try {
//
//            mmr.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/raw/cage_the_elephant_trouble"))
//
//            //mmr.setDataSource(this, uri)
//        }catch (e : IllegalArgumentException){
//            //title = "error"
//        }
//        var artist = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
//        var title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
//        songTitle.text = title
//        author.text = artist
//        var hasImage = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_COUNT)?.toInt()?:-1
//        if(hasImage>0  ){
//            var imageAtIndex:Int =
//                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_IMAGE_PRIMARY)?.toInt() ?:-1
//            //if(imageAtIndex!=-1)
//                songImage.setImageBitmap(mmr.getImageAtIndex(imageAtIndex) )
//
//        }
//
//        val artBytes = mmr.embeddedPicture
//        if (artBytes != null) {
//            val bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
//            songImage.setImageBitmap(bm)
//        }
//
//    }
//
//

//
}