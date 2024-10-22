package es.usj.androidapps.alu100485.movieslibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import es.usj.androidapps.alu100485.movieslibrary.R
import es.usj.androidapps.alu100485.movieslibrary.adapters.AdapterActors
import kotlinx.android.synthetic.main.fragment_actors.*

// i assign the adapter for eac fragment
class FragmentActors : Fragment() {

    var adapterActors: AdapterActors? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapterActors = AdapterActors()
        adapterActors?.loadData()
    }

    override fun onResume() {
        super.onResume()
        adapterActors?.loadData() //this is for updating elements eac time that the activity loads, and reseting with updated data
        adapterActors?.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvActors.layoutManager = LinearLayoutManager(context)
        rvActors.setHasFixedSize(true)
        rvActors.itemAnimator = DefaultItemAnimator()
        rvActors.adapter = adapterActors
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_actors, container, false)
    }
}