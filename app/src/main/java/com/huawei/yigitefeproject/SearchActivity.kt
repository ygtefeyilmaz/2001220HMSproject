package com.huawei.yigitefeproject

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.location.LocationServices
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.location.nlp.network.response.Location
import com.huawei.yigitefeproject.databinding.ActivitySearchBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        supportActionBar?.title = "Search Charging Stations"

        val user = AGConnectAuth.getInstance().currentUser

        val userName = user.displayName
        if (user != null) {
            val userName = user.displayName                 //display user display name on textview
        }
        binding.userinfo.setText("$userName")


        val countryCodes = arrayOf("Select Country: ", "TR", "GB", "USA", "FR", "DE", "CA", "AU", "GR", "ES")

        val adapter = ArrayAdapter(
            this@SearchActivity,
            android.R.layout.simple_spinner_item,
            countryCodes
        )

         binding.countrySpinner.adapter = adapter

        binding.btnSearch.setOnClickListener {
            if (binding.countrySpinner.selectedItemPosition > 0) {       //checks any item is selected or not

                val intent = Intent(this, MapActivity::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "Please select a country code", Toast.LENGTH_SHORT).show()
            }

        }






        // Dynamically apply for required permissions if the API level is 28 or smaller.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "android sdk <= 28 Q")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            }
        } else {
            // Dynamically apply for required permissions if the API level is greater than 28. The android.permission.ACCESS_BACKGROUND_LOCATION permission is required.
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    "android.permission.ACCESS_BACKGROUND_LOCATION"
                )
                ActivityCompat.requestPermissions(this, strings, 2)
            }
        }


        /*   val retrofit = Retrofit.Builder()
                 .baseUrl("https://api.openchargemap.io/v3/poi?key=1111")
                 .addConverterFactory(GsonConverterFactory.create())
                 .build()
             val service = retrofit.create(OpenChargeMapService::class.java) */


        /*   GlobalScope.launch(Dispatchers.Main) {
               val countries = service.getCountries()
               val countryArray = countries.map { it.Title }.toTypedArray()

               val adapter = ArrayAdapter(
                   this@SearchActivity,
                   android.R.layout.simple_spinner_item,
                   countryArray
               )
             //  binding.countrySpinner.adapter = adapter
           } */

        /*     mFusedLocationProviderClient =
                 LocationServices.getFusedLocationProviderClient(this)

             binding.btnLocation.setOnClickListener {
                   getLastKnownLocation()
             lastKnownLocation.observe(this, Observer {
                 binding.locationtw.text = it?.toString()
             })

               } */
        /*   lastKnownLocation.observe(this, Observer {
               binding.locationtw.text = it?.toString()
           }) */

    }

  /*  private lateinit var FusedLocationProviderClient: FusedLocationProviderClient


    fun getLastLocation(lastKnownLocation: (LatLng) -> Unit) {
        val mFusedLocationProviderClient: FusedLocationProviderClient? =
            LocationServices.getFusedLocationProviderClient(this)

        var latLng: LatLng
        try {
            val lastLocation = mFusedLocationProviderClient?.lastLocation
            lastLocation?.addOnSuccessListener {
                if (it == null) {
                    latLng = LatLng(0.0, 0.0)
                    lastKnownLocation(latLng)
                } else {
                    latLng = LatLng(it.latitude, it.longitude)
                    lastKnownLocation(latLng)
                }
            }?.addOnFailureListener {
                Log.e("LocationService", "getLastLocation exception:" + it.message)
            }
        } catch (e: Exception) {
            Log.e("LocationService", "getLastLocation exception:" + e.message)
        }
    }






     fun checkLocationSettings(lastKnownLocation: (LatLng) -> Unit) {
         var latLng: LatLng
         val settingsClient = LocationServices.getSettingsClient(this)
         val mLocationCallback: LocationCallback
         mLocationCallback = object : LocationCallback() {

             override fun onLocationResult(locationResult: LocationResult) {
                 latLng = LatLng(
                     locationResult.lastHWLocation.latitude,
                     locationResult.lastHWLocation.longitude
                 )
                 lastKnownLocation(latLng)
             }
         }

         val builder = LocationSettingsRequest.Builder()
         val mLocationRequest = LocationRequest()
         builder.addLocationRequest(mLocationRequest)
         val locationSettingsRequest = builder.build()
         settingsClient.checkLocationSettings(locationSettingsRequest)
             .addOnSuccessListener {
                     locationSettingsResponse ->
                 val locationSettingsStates = locationSettingsResponse.locationSettingsStates
                 val stringBuilder = StringBuilder()

                 stringBuilder.append("isLocationUsable=")
                     .append(locationSettingsStates.isLocationUsable)

                 stringBuilder.append(",\nisHMSLocationUsable=")
                     .append(locationSettingsStates.isHMSLocationUsable)

                 Log.i(TAG, "checkLocationSetting onComplete:$stringBuilder")

             }
             .addOnFailureListener(OnFailureListener { e ->
                 Log.i(TAG, "checkLocationSetting onFailure:" + e.message)
             })

     }

    private val _lastKnownLocation = MutableLiveData<LatLng?>()
    val lastKnownLocation: LiveData<LatLng?>

        get() = _lastKnownLocation

    fun setLastKnownLocation(latLng: LatLng?) {
        _lastKnownLocation.value = latLng
    }

    private fun getLastKnownLocation() {
        getLastLocation { lastLocation ->
            if (lastLocation.latitude != 0.0) {
                setLastKnownLocation(lastLocation)
            } else {
                checkLocationSettings {
                   setLastKnownLocation(it)
                }
            }
        }
    }

*/

    /* fun getLastLocation(lastKnownLocation: (LatLng) -> Unit) {

         val mFusedLocationProviderClient: FusedLocationProviderClient? = LocationServices.getFusedLocationProviderClient(this)

         var latLng: LatLng
         try {
             val lastLocation: Task<android.location.Location>? = mFusedLocationProviderClient!!.lastLocation
             lastLocation?.addOnSuccessListener {
                 if (it == null) {
                     latLng = LatLng(0.0, 0.0)
                     lastKnownLocation(latLng)
                 } else {
                     latLng = LatLng(it.latitude, it.longitude)
                     lastKnownLocation(latLng)
                 }
             }?.addOnFailureListener {
                 Log.e("LocationService", "getLastLocation exception:" + it.message)
             }
         } catch (e: Exception) {
             Log.e("LocationService", "getLastLocation exception:" + e.message)
         }
     }




     private val _lastKnownLocation = MutableLiveData<LatLng?>()
     val lastKnownLocation: LiveData<LatLng?>
         get() = _lastKnownLocation

     fun setLastKnownLocation(latLng: LatLng?) {
         _lastKnownLocation.value = latLng
     }


 */


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_logout) {
            logout()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun logout() {
        AGConnectAuth.getInstance().signOut()
        val intent = Intent(this@SearchActivity, MainActivity::class.java)
        startActivity(intent)


    }
    fun isNumeric(input: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return input.matches(regex)
    }
}

