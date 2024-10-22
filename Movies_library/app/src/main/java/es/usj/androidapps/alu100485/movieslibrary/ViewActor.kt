package es.usj.androidapps.alu100485.movieslibrary

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import es.usj.androidapps.alu100485.movieslibrary.adapters.AdapterMoviesViewActor
import es.usj.androidapps.alu100485.movieslibrary.constants.DELETE
import es.usj.androidapps.alu100485.movieslibrary.constants.EDIT
import es.usj.androidapps.alu100485.movieslibrary.constants.deleteMovie
import es.usj.androidapps.alu100485.movieslibrary.constants.movies
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Actor
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import kotlinx.android.synthetic.main.activity_view_actor.*
//here i display the info of the actor and its movies
class ViewActor : AppCompatActivity() {

    private lateinit var actor: Actor
    private var moviesLocal: ArrayList<Movie> = arrayListOf()
    private var adapterMoviesViewActor: AdapterMoviesViewActor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_actor)
        adapterMoviesViewActor = AdapterMoviesViewActor()

        actor = intent.getParcelableExtra("actorInfo")
        setTextViews()
        getMovies()
        settingAdapters()
    }

    private fun getMovies(){
        for (i in 0 until movies().size){
            for (j in 0 until movies()[i].actors!!.size){
                if (movies()[i].actors!![j] == actor.id)
                    moviesLocal.add(movies()[i])
            }
        }
    }

    private fun setTextViews(){
        tvGenreNameViewGenre.text = actor.name
        tvGenreIdViewGenre.text = actor.id.toString()
    }

    override fun onResume() {
        super.onResume()
        adapterMoviesViewActor?.setData(moviesLocal)
        adapterMoviesViewActor?.notifyDataSetChanged()
    }

    private fun settingAdapters(){
        adapterMoviesViewActor?.setData(moviesLocal)
        rvMoviesViewGenres.layoutManager = LinearLayoutManager(applicationContext)
        rvMoviesViewGenres.setHasFixedSize(true)
        rvMoviesViewGenres.itemAnimator = DefaultItemAnimator()
        rvMoviesViewGenres.adapter = adapterMoviesViewActor
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
                adapterMoviesViewActor?.notifyItemChanged(item.groupId)
                adapterMoviesViewActor?.setData(moviesLocal)
                return true
            }
            else ->  super.onContextItemSelected(item)
        }
    }
}