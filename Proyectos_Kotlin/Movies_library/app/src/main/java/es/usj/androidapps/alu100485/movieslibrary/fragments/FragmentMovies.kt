package es.usj.androidapps.alu100485.movieslibrary.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import es.usj.androidapps.alu100485.movieslibrary.*
import es.usj.androidapps.alu100485.movieslibrary.adapters.AdapterMovies
import es.usj.androidapps.alu100485.movieslibrary.constants.*
import kotlinx.android.synthetic.main.fragment_movies.*

//adding the adapter for the fragments of the list of movies
class FragmentMovies : Fragment() {

    var adapterMovies: AdapterMovies? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapterMovies = AdapterMovies()
        adapterMovies?.loadData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvMovieList.layoutManager = LinearLayoutManager(context)
        rvMovieList.setHasFixedSize(true)
        rvMovieList.itemAnimator = DefaultItemAnimator()
        rvMovieList.adapter = adapterMovies
        activity?.registerForContextMenu(rvMovieList)
    }

    override fun onResume() {
        super.onResume()
        adapterMovies?.loadData() //this is for updating elements eac time that the activity loads, and reseting with updated data
        adapterMovies?.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.title) {
            EDIT -> {
                val movieSelected = getSelectedMovie(item.groupId, adapterMovies!!.list) // i search the correct movie, then i edit it
                val intentToEditMovie = Intent(activity?.applicationContext, EditMovie::class.java)
                intentToEditMovie.putExtra("movieEditInfo", movieSelected)
                activity?.startActivity(intentToEditMovie)
                return true
            }
            DELETE -> {
                deleteMovie(activity!!.applicationContext, item.groupId, adapterMovies!!.list)
                adapterMovies?.notifyDataSetChanged()
                adapterMovies?.loadData()
                return true
            }
            else ->  super.onContextItemSelected(item)
        }
    }
}