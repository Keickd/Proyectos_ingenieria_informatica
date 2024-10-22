package es.usj.androidapps.alu100485.ho

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.telephony.PhoneNumberUtils.formatNumber
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import kotlinx.android.synthetic.main.activity_b.*
import kotlinx.android.synthetic.main.activity_c.*
import java.util.regex.Pattern

class Activity_c : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c)

        var id = intent.extras?.getString("id")
        tvCompanyData.setText(id)

        etPhone.addTextChangedListener(CustomTextWatcher({
            if(etPhone.text.toString().length == 9){
                btnCall.isEnabled = true
                btnCall.isClickable = true
            }else{
                btnCall.isEnabled = false
                btnCall.isClickable = false
            }
        }))

        etEmail.addTextChangedListener(CustomTextWatcher({
            /*if(isEmailValid(etEmail.text.toString())){
                btnSend.isEnabled = true
                btnSend.isClickable = true
            }else{
                btnSend.isEnabled = true
                btnSend.isClickable = true
            }*/
            if (etEmail.text.toString() != ""){
                btnSend.isEnabled = true
                btnSend.isClickable = true
            }else{
                btnSend.isEnabled = false
                btnSend.isClickable = false
            }
        }))

        etUrl.addTextChangedListener(CustomTextWatcher({
            if (etUrl.text.toString() != ""){
                btnOpen.isEnabled = true
                btnOpen.isClickable = true
            }else{
                btnOpen.isEnabled = false
                btnOpen.isClickable = false
            }
        }))

        btnCall.setOnClickListener {
            val phoneNumber = etPhone.text.toString()
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
        }

        btnSend.setOnClickListener {
            val email = etEmail.text.toString()
            startActivity(Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null)))
        }

        btnOpen.setOnClickListener {
            var url = etUrl.text.toString()
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://$url"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    fun isEmailValid(email: String): Boolean {
        return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }
}