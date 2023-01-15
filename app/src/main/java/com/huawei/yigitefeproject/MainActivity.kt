package com.huawei.yigitefeproject

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.huawei.agconnect.api.AGConnectApi
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton
import com.huawei.yigitefeproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        supportActionBar?.title = "2001220"

        val user = AGConnectAuth.getInstance().currentUser


    lateinit var authParams: HuaweiIdAuthParams
        authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
        .setAccessToken().createParams()


        binding.loginbutton.setOnClickListener {
            AGConnectAuth.getInstance()

                .signIn(this@MainActivity, AGConnectAuthCredential.HMS_Provider)
                .addOnSuccessListener {
                    val intent = Intent(this@MainActivity, SearchActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this,it.message.toString(), Toast.LENGTH_SHORT).show()
                }

        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AGConnectApi.getInstance().activityLifecycle().onActivityResult(requestCode,resultCode,data)
    }

}