package es.usj.androidapps.alu100485.movieslibrary.asynctask.get

import android.content.Intent
import android.os.AsyncTask
import es.usj.androidapps.alu100485.movieslibrary.Home
import es.usj.androidapps.alu100485.movieslibrary.MainActivity
//This class is a common point where the main async task agree, when all of them are finished then this will be executed, this is only for synchronizing the code
class LoadDataAsync(context: MainActivity): AsyncTask<Void, Void, Void>() {

    lateinit var loadingMovies: LoadMoviesAsync
    lateinit var loadingActors: LoadActorsAsync
    lateinit var loadingGenres: LoadGenresAsync
    val context = context

    override fun onPreExecute() {
        super.onPreExecute()

        loadingMovies = LoadMoviesAsync(this)
        loadingActors = LoadActorsAsync(this)
        loadingGenres = LoadGenresAsync(this)

        loadingMovies.execute()
        loadingActors.execute()
        loadingGenres.execute()
    }


    override fun doInBackground(vararg p0: Void?): Void? {

        loadingMovies.get()
        loadingActors.get()
        loadingGenres.get()

        return  null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        val intentFromSplashToMovieList = Intent(context, Home::class.java)
        context.startActivity(intentFromSplashToMovieList) //then i 'll launch the splash
        context.finish()
    }

}