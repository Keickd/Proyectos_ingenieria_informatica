package es.usj.androidapps.alu100485.movieslibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import es.usj.androidapps.alu100485.movieslibrary.R
import es.usj.androidapps.alu100485.movieslibrary.adapters.AdapterGenres
import kotlinx.android.synthetic.main.fragment_genres.*
//adding the adapter for the fragments of the list of genres
class FragmentGenres : Fragment() {

    var adapterGenres: AdapterGenres? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapterGenres = AdapterGenres()
        adapterGenres?.loadData()
    }

    override fun onResume() {
        super.onResume()
        adapterGenres?.loadData() //this is for updating elements eac time that the activity loads, and reseting with updated data
        adapterGenres?.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvGenres.layoutManager = LinearLayoutManager(context)
        rvGenres.setHasFixedSize(true)
        rvGenres.itemAnimator = DefaultItemAnimator()
        rvGenres.adapter = adapterGenres
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_genres, container, false)
    }
}