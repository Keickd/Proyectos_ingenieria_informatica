package es.usj.androidapps.alu100485.ho

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var userOk = false
    var userPass = false
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private var mFirebaseRemoteConfig: FirebaseRemoteConfig? = null
    // Remote Config keys
    private val BTNLOGIN = "btnlogin"
    private val BTNREGISTER = "btnregister"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = Firebase.analytics
        mFirebaseAnalytics.setUserProperty("user_type", "standard")

        mFirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        mFirebaseRemoteConfig!!.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig!!.setDefaultsAsync(R.xml.remote_config_defaults)
        //We will execute the next part in "onResume"

        etUser.addTextChangedListener(CustomTextWatcher({
            if (!etUser.text.isNullOrBlank()) userOk = true
            else userOk = false

            checkUserAndPass()
        }))

        etPassword.addTextChangedListener(CustomTextWatcher({
            if (!etPassword.text.isNullOrBlank()) userPass = true
            else userPass = false

            checkUserAndPass()
        }))

        btnLogin.setOnClickListener {
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, null)
            val intentFromAToB = Intent(this, Activity_b::class.java)
            startActivity(intentFromAToB)
        }

        btnRegister.setOnClickListener {
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, null)
            Toast.makeText(this, "You are already registered", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        super.onResume()
        updateData()
    }


    private fun updateData(){
        mFirebaseRemoteConfig!!.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("MainActivity", "Config params updated: $updated")
                    Toast.makeText(
                        this, "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT
                    ).show()
                    val btnLoginButton = mFirebaseRemoteConfig!![BTNLOGIN].asString()
                    val btnRegisterButton = mFirebaseRemoteConfig!![BTNREGISTER].asString()
                    btnLogin.setText(btnLoginButton)
                    btnRegister.setText(btnRegisterButton)
                } else {
                    Toast.makeText(
                        this, "Fetch failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun checkUserAndPass(){
        if(userOk && userPass){
            btnLogin.isEnabled = true
            btnLogin.isClickable = true
        }else{
            btnLogin.isEnabled = false
            btnLogin.isClickable = false
        }
    }

}

class CustomTextWatcher(private val callback: () -> Unit) : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
    override fun afterTextChanged(s: Editable?) { }
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        this.callback()
    }
}
