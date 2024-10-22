package es.usj.androidapps.alu100485.movieslibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import es.usj.androidapps.alu100485.movieslibrary.adapters.AdapterMoviesViewGenre
import es.usj.androidapps.alu100485.movieslibrary.constants.DELETE
import es.usj.androidapps.alu100485.movieslibrary.constants.EDIT
import es.usj.androidapps.alu100485.movieslibrary.constants.deleteMovie
import es.usj.androidapps.alu100485.movieslibrary.constants.movies
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Genre
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import kotlinx.android.synthetic.main.activity_view_actor.*
import kotlinx.android.synthetic.main.activity_view_actor.rvMoviesViewGenres
//It is similar to  view actor
class ViewGenre : AppCompatActivity() {
    private lateinit var genre: Genre
    private var moviesLocal: ArrayList<Movie> = arrayListOf()
    var adapterMoviesViewGenre: AdapterMoviesViewGenre? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_actor)

        genre = intent.getParcelableExtra("genreInfo")
        setTextViews()
        getMovies()
        settingAdapters()
    }

    private fun getMovies(){ //get movies from this genre
        for (i in movies().indices){
            for (j in 0 until movies()[i].genres!!.size){
                if (movies()[i].genres!![j] == genre.id)
                    moviesLocal.add(movies()[i])
            }
        }
    }

    private fun setTextViews(){
        tvGenreNameViewGenre.text = genre.name
        tvGenreIdViewGenre.text = genre.id.toString()
    }

    private fun settingAdapters(){
        adapterMoviesViewGenre = AdapterMoviesViewGenre()
        adapterMoviesViewGenre!!.setData(moviesLocal)
        rvMoviesViewGenres.layoutManager = LinearLayoutManager(applicationContext)
        rvMoviesViewGenres.setHasFixedSize(true)
        rvMoviesViewGenres.itemAnimator = DefaultItemAnimator()
        rvMoviesViewGenres.adapter = adapterMoviesViewGenre
    }

    override fun onResume() {
        super.onResume()
        adapterMoviesViewGenre?.setData(moviesLocal) //loading data after each modification
        adapterMoviesViewGenre?.notifyDataSetChanged()
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.title) {
            EDIT -> {
                val movieSelected = moviesLocal[item.groupId]
                val intentToEditMovie = Intent(applicationContext, EditMovie::class.java)
                intentToEditMovie.putExtra("movieEditInfo", movieSelected)
                startActivity(intentToEditMovie)
                return true
            }
            DELETE -> {
                deleteMovie(applicationContext, item.groupId, moviesLocal)
                moviesLocal.removeAt(item.groupId)
                adapterMoviesViewGenre?.notifyItemChanged(item.groupId)
                adapterMoviesViewGenre?.setData(moviesLocal)
                return true
            }
            else ->  super.onContextItemSelected(item)
        }
    }
}