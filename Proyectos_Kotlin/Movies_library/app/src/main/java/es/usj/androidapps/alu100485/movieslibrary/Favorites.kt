package es.usj.androidapps.alu100485.movieslibrary

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import es.usj.androidapps.alu100485.movieslibrary.adapters.AdapterFavMovies
import es.usj.androidapps.alu100485.movieslibrary.constants.DELETE
import es.usj.androidapps.alu100485.movieslibrary.constants.EDIT
import es.usj.androidapps.alu100485.movieslibrary.constants.deleteMovie
import es.usj.androidapps.alu100485.movieslibrary.constants.favs
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import kotlinx.android.synthetic.main.activity_favorites.*

class Favorites : AppCompatActivity() {

    lateinit var adapterFavMovies: AdapterFavMovies

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        adapterFavMovies = AdapterFavMovies()
        settingAdapters()
    }

    override fun onResume() {
        super.onResume()
        adapterFavMovies?.notifyDataSetChanged()
    }

    private fun settingAdapters(){
        rvFavorites.layoutManager = LinearLayoutManager(applicationContext)
        rvFavorites.setHasFixedSize(true)
        rvFavorites.itemAnimator = null
        rvFavorites.adapter = adapterFavMovies
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.title) {
            EDIT -> {
                val movieSelected = favs()[item.groupId]
                val intentToEditMovie = Intent(applicationContext, EditMovie::class.java)
                intentToEditMovie.putExtra("movieEditInfo", movieSelected)
                startActivity(intentToEditMovie)
                return true
            }
            DELETE -> {
                var favs = arrayListOf<Movie>()
                favs().toCollection(favs)
                deleteMovie(applicationContext, item.groupId, favs)
                adapterFavMovies?.notifyItemChanged(item.groupId)
                adapterFavMovies?.loadData()
                return true
            }
            else ->  super.onContextItemSelected(item)
        }
    }
}