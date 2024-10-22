package es.usj.androidapps.alu100485.movieslibrary

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import es.usj.androidapps.alu100485.movieslibrary.adapters.AdapterActorsViewMovie
import es.usj.androidapps.alu100485.movieslibrary.adapters.AdapterGenresViewMovie
import es.usj.androidapps.alu100485.movieslibrary.constants.loadImageForViews
import es.usj.androidapps.alu100485.movieslibrary.constants.manageFavorites
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Actor
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Genre
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import es.usj.androidapps.alu100485.movieslibrary.singlenton.ActorsSingleton
import es.usj.androidapps.alu100485.movieslibrary.singlenton.GenresSingleton
import kotlinx.android.synthetic.main.activity_view_movie.*


class ViewMovie : AppCompatActivity() {

    lateinit var movie: Movie
    var actors: ArrayList<Actor> = arrayListOf()
    var genres: ArrayList<Genre> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_movie)

        settingScrolls()
        movie = intent.getParcelableExtra("movieInfo")

        loadImageForViews(applicationContext, movie, ivCover) //loading image

        manageFavorites(movie, ibtnFavoriteEditMovie, applicationContext)
        getNames()
        settingAdapters()
        fulfillMovieInfo()
    }

    private fun getNames(){
        for(i in 0 until ActorsSingleton.actors.size){
            for(j in 0 until movie.actors!!.size){
                if(ActorsSingleton.actors[i].id == movie.actors!![j])
                    actors.add(ActorsSingleton.actors[i])
            }
        }

        for(i in 0 until GenresSingleton.genres.size){
            for(j in 0 until movie.genres!!.size){
                if(ActorsSingleton.actors[i].id == movie.genres!![j])
                    genres.add(GenresSingleton.genres[i])
            }
        }
    }

    private fun settingScrolls(){
        tvTitle.movementMethod = ScrollingMovementMethod()
        tvDirector.movementMethod = ScrollingMovementMethod()
        tvDescriptionContent.movementMethod = ScrollingMovementMethod()
    }

    private fun settingAdapters(){

        val adapterActorsViewMovie = AdapterActorsViewMovie()
        adapterActorsViewMovie.setData(actors)
        rvMoviesFromActorsViewMovie.layoutManager = LinearLayoutManager(applicationContext)
        rvMoviesFromActorsViewMovie.setHasFixedSize(true)
        rvMoviesFromActorsViewMovie.itemAnimator = DefaultItemAnimator()
        rvMoviesFromActorsViewMovie.adapter = adapterActorsViewMovie

        val adapterGenresViewMovie = AdapterGenresViewMovie()
        adapterGenresViewMovie.setData(genres)
        rvMoviesFromGenresViewMovie.layoutManager = LinearLayoutManager(applicationContext)
        rvMoviesFromGenresViewMovie.setHasFixedSize(true)
        rvMoviesFromGenresViewMovie.itemAnimator = DefaultItemAnimator()
        rvMoviesFromGenresViewMovie.adapter = adapterGenresViewMovie
    }

    @SuppressLint("SetTextI18n")
    private fun fulfillMovieInfo(){
        tvId.text = "Id: " + movie.id.toString()
        tvTitle.text = movie.title
        tvDescriptionContent.text = movie.description
        tvDirector.text = movie.director
        tvYear.text = "Year: " + movie.year.toString()
        tvRuntime.text = "Length: " + movie.runtime.toString()
        tvRating.text = "Rating: " + movie.rating.toString()
        tvVotes.text = "Votes: " + movie.votes.toString()
        tvRevenue.text = "Revenue: " + movie.revenue.toString()
    }
}