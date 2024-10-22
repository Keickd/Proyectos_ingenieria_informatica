package es.usj.androidapps.alu100485.movieslibrary

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_contact.*

class Contact : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        callButton()
        emailButton() //setting up some listeners
        urlOpenButton()
    }

    private fun callButton(){
        ibtnPhone.setOnClickListener {
            val phoneNumber = "+34609135212"
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
        }
    }

    private fun emailButton(){
        ibtnMail.setOnClickListener {
            val email = "alu.100485@usj.es"
            startActivity(Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null)))
        }
    }

    private fun urlOpenButton(){
        ibtnWeb.setOnClickListener {
            val url = "https://cursokotlin.com/"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }
}