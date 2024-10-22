package es.usj.androidapps.alu100485.movieslibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.usj.androidapps.alu100485.movieslibrary.asynctask.get.LoadDataAsync


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoadDataAsync(this).execute() //i execute the main async task
    }
}



