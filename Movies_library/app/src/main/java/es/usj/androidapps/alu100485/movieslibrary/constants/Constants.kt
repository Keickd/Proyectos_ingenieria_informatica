package es.usj.androidapps.alu100485.movieslibrary.constants

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import es.usj.androidapps.alu100485.movieslibrary.R
import es.usj.androidapps.alu100485.movieslibrary.asynctask.delete.DeleteMovieAsyncTask
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import es.usj.androidapps.alu100485.movieslibrary.singlenton.ActorsSingleton
import es.usj.androidapps.alu100485.movieslibrary.singlenton.GenresSingleton
import es.usj.androidapps.alu100485.movieslibrary.singlenton.MoviesSingleton
import kotlinx.android.synthetic.main.item_movie.view.*
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

const val SERVER = "172.18.68.148"//"192.168.1.31"//"10.0.2.2"//"192.168.1.31"// //or your computer local wifi address if we use a phone

const val USER = "admin"

const val PASSWORD = "admin"

const val GETMOVIES = "getMovies"

const val GETACTORS = "getActors"

const val GETGENRES = "getGenres"

const val ADDACTOR = "addActor"

const val ADDMOVIE = "addMovie"

const val ADDGENRE = "addGenre"

const val UPDATEACTOR = "updateActor"

const val UPDATEMOVIE = "updateMovie"

const val UPDATEGENRE = "updateGenre"

const val DELETEMOVIE = "deleteMovie"

const val APIKEY = "10846b22"

const val EDIT  = "Edit"

const val DELETE  = "Delete"

fun movies() = MoviesSingleton.movies.sortedBy { it.id }.toMutableList()

fun actors() = ActorsSingleton.actors.sortedBy { it.id }.toMutableList()

fun genres() = GenresSingleton.genres.sortedBy { it.id }.toMutableList()

fun favs() = MoviesSingleton.movies.filter { item -> item.favorite }.sortedBy { it.id }

fun checkFavorites(itemView: View, movie: Movie){
    if(movie.favorite)
        itemView.btnFavoriteItem.setImageResource(R.drawable.ic_favorite_filled)
    else
        itemView.btnFavoriteItem.setImageResource(R.drawable.ic_favorite_empty)
}

fun hideKeyboard(activity: Activity) {
    val view = activity.findViewById<View>(android.R.id.content)
    if (view != null) {
        val imm = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
// Depending on the moment i have to load the images inside adapters for list or inside a normal activity to display one image
fun loadImageForViews(context: Context, movie: Movie, imgView: ImageView){
    try {
        val cw = ContextWrapper(context)
        val directory = cw.getDir("MovieCovers", Context.MODE_PRIVATE)
        val f = File(directory, "${movie.id}.jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
        if(b != null)
            imgView.setImageBitmap(b)
        else
            imgView.setImageResource(R.drawable.no_image)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}

fun loadImageForAdapters(itemView: View, movie: Movie){
    try {
        val cw = ContextWrapper(itemView.context)
        val directory = cw.getDir("MovieCovers", Context.MODE_PRIVATE)
        val f = File(directory, "${movie.id}.jpg")
        val b = BitmapFactory.decodeStream(FileInputStream(f))
        if(b != null)
            itemView.ivMovie.setImageBitmap(b)
        else
            itemView.ivMovie.setImageResource(R.drawable.no_image)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    }
}

fun checkDownloadedCover(itemView: View, movie: Movie): Boolean{ //checking if the image is in memory
    val cw = ContextWrapper(itemView.context)
    val directory = cw.getDir("MovieCovers", Context.MODE_PRIVATE)
    val f = File(directory.toString())
    val files = f.listFiles()
    for (i in files){
        if(i.name == "${movie.id}.jpg")
            return true
    }
    return false
}

fun deleteMovie(context: Context, pos: Int, moviesLocal: ArrayList<Movie>) {
    for(i in 0 until MoviesSingleton.movies.size){
        if(MoviesSingleton.movies[i].id == moviesLocal[pos].id) {
            deleteMovieIntoDatabase(context, MoviesSingleton.movies[i])
            DeleteMovieAsyncTask().execute(MoviesSingleton.movies[i])
            deleteCover(context, MoviesSingleton.movies[i])
            MoviesSingleton.movies.removeAt(i)
            return
        }
    }
}

fun manageFavorites(movie: Movie, imageView: ImageView, context: Context){
    if (movie.favorite)
        imageView.setImageResource(R.drawable.ic_favorite_filled)
    else
        imageView.setImageResource(R.drawable.ic_favorite_empty)

    imageView.setOnClickListener {
        if (movie.favorite){
            movie.favorite = false
            updateMovieIntoDatabase(context, movie)
            imageView.setImageResource(R.drawable.ic_favorite_empty)
        }else{
            movie.favorite = true
            updateMovieIntoDatabase(context, movie)
            imageView.setImageResource(R.drawable.ic_favorite_filled)
        }
        updateMoviesSingleton(movie)
    }
}

fun updateMoviesSingleton(movie: Movie){
    for (i in 0 until MoviesSingleton.movies.size){
        if(MoviesSingleton.movies[i].id == movie.id){
            MoviesSingleton.movies[i].favorite = movie.favorite
            return
        }
    }
}

fun deleteCover(context: Context, movie: Movie){
    val cw = ContextWrapper(context)
    val directory = cw.getDir("MovieCovers", Context.MODE_PRIVATE)
    val f = File(directory.toString())
    val files = f.listFiles()
    for (i in files){
        if(i.name == "${movie.id}.jpg") {
            i.delete()
            return
        }
    }
}

fun getSelectedMovie(pos: Int, moviesLocal: ArrayList<Movie>): Movie? {
    for(i in 0 until MoviesSingleton.movies.size){
        if(MoviesSingleton.movies[i].id == moviesLocal[pos].id) {
            return MoviesSingleton.movies[i]
        }
    }
    return null
}