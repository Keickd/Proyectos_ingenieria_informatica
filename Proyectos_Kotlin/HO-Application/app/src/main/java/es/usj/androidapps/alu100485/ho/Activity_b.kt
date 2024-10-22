package es.usj.androidapps.alu100485.ho

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import android.widget.Toast
import es.usj.androidapps.alu100485.ho.databinding.ActivityBBinding
import kotlinx.android.synthetic.main.activity_b.*
import kotlinx.android.synthetic.main.activity_main.*

class Activity_b : AppCompatActivity() {

    var companyIDOk = false
    var spinnerOk = false
    var nameCitizenOk = false
    var surnameCitizen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)

        val spinnerData = arrayOf("Country","Spain","USA","UK","JAPAN")
        spCountry.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerData)
        spCountry.setSelection(0)
        etCompanyID.addTextChangedListener(CustomTextWatcher({
            if(!etCompanyID.text.isNullOrBlank()) companyIDOk = true
            else companyIDOk = false

            checkCompany()
        }))

        spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(position != 0 ) spinnerOk = true
                else spinnerOk = false

                checkCompany()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        etNameCitizen.addTextChangedListener(CustomTextWatcher({
            if(!etNameCitizen.text.isNullOrBlank()) nameCitizenOk = true
            else nameCitizenOk = false

            checkCitizen()
        }))

        etSurnameCitizen.addTextChangedListener(CustomTextWatcher({
            if(!etSurnameCitizen.text.isNullOrBlank()) surnameCitizen = true
            else surnameCitizen = false

            checkCitizen()
        }))

        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            if(rbtnCitizen.isChecked){
                etCompanyID.visibility = GONE
                spCountry.visibility = GONE
                btnInitOpCompany.visibility = GONE

                etNameCitizen.visibility = VISIBLE
                etSurnameCitizen.visibility= VISIBLE
                btnInitOpCitizen.visibility = VISIBLE
            }else {
                rbtnCompany.setOnClickListener {
                    etNameCitizen.visibility = GONE
                    etSurnameCitizen.visibility= GONE
                    btnInitOpCitizen.visibility = GONE

                    etCompanyID.visibility = VISIBLE
                    spCountry.visibility = VISIBLE
                    btnInitOpCompany.visibility = VISIBLE
                }
            }
        })

        btnInitOpCompany.setOnClickListener {
            val intentFromBToC = Intent(this, Activity_c::class.java)
            intentFromBToC.putExtra("id",etCompanyID.text.toString())
            startActivity(intentFromBToC)
        }

        btnInitOpCitizen.setOnClickListener {
            val intentFromBToD = Intent(this, Activity_d::class.java)
            intentFromBToD.putExtra("clientName",etNameCitizen.text.toString())
            startActivity(intentFromBToD)
        }

    }

    private fun checkCitizen(){
        if(nameCitizenOk && surnameCitizen){
            btnInitOpCitizen.isEnabled = true
            btnInitOpCitizen.isClickable = true
        }else{
            btnInitOpCitizen.isEnabled = false
            btnInitOpCitizen.isClickable = false
        }
    }

    private fun checkCompany(){
        if(companyIDOk && spinnerOk){
            btnInitOpCompany.isEnabled = true
            btnInitOpCompany.isClickable = true
        }else{
            btnInitOpCompany.isEnabled = false
            btnInitOpCompany.isClickable = false
        }
    }
}
