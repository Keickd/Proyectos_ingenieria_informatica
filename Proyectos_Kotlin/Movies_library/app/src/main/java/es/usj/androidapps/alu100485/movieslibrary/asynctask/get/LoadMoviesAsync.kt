package es.usj.androidapps.alu100485.movieslibrary.asynctask.get

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import es.usj.androidapps.alu100485.movieslibrary.*
import es.usj.androidapps.alu100485.movieslibrary.constants.*
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import es.usj.androidapps.alu100485.movieslibrary.singlenton.MoviesSingleton
import es.usj.androidapps.alu100485.movieslibrary.sqlite.AdminSQLiteOpenHelper
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class LoadMoviesAsync(val context: LoadDataAsync): AsyncTask<Void, Int, ArrayList<Movie>>() {
    var movies: ArrayList<Movie> = arrayListOf()
    var databaseAvailable = checkDatabaseAvailable(context.context, TABLEMOVIES)
    override fun doInBackground(vararg p0: Void?): ArrayList<Movie> {

        if(databaseAvailable){ //First i check the availability of the database, i search the movies from here and i get the data from the api too,
            movies = searchMovies() // then i fetch to get the data from the api updating the database values
            var moviesFromApiFetch = arrayListOf<Movie>()
            loadingMovies().toCollection(moviesFromApiFetch)
            fetchMovies(moviesFromApiFetch)
        }else
            loadingMovies().toCollection(movies)

        return movies
    }

    override fun onPostExecute(result: ArrayList<Movie>) {
        super.onPostExecute(result)

        MoviesSingleton.movies.addAll(movies) //in all times i will go here and fill the singleton
        if(!databaseAvailable)
            insertMoviesToDatabase() //fulfill all database


    }

    private fun fetchMovies(moviesFetch: ArrayList<Movie>){
        var favoriteList = arrayListOf<Movie>()
        var finalMovies = arrayListOf<Movie>()
        if(moviesFetch.size != 0){

            movies.forEach { it ->
                //finalMovies.add(it)
                if (it.favorite){
                    favoriteList.add(it)
                }
            }
            //this is made in this way because of the favorites i need to make an extra array and then i will check what elements will be favorites or not, besides updating database
            var trigger = 0
            for(i in 0 until moviesFetch.size){
                for (j in 0 until movies.size){
                    if (moviesFetch[i].id == movies[j].id){
                        if(movies[j].favorite)
                            moviesFetch[i].favorite = true
                        updateMovieIntoDatabase(context.context, moviesFetch[i])
                        finalMovies.add(moviesFetch[i])
                        trigger = 1
                    }
                }
                if (trigger == 0){
                    finalMovies.add(moviesFetch[i])
                    insertMovieToDatabase(moviesFetch[i])
                }
                trigger = 0
            }

            movies.forEach { it ->
                it.favorite = false
            }

            /*movies.forEach { it ->
                if (!moviesFetch.contains(it)) {
                    finalMovies.remove(it)
                    deleteMovieIntoDatabase(context.context, it)
                }
            }*/

            trigger = 0
            for(i in 0 until movies.size){
                for (j in 0 until moviesFetch.size){
                    if (movies[i].id == moviesFetch[j].id)
                        trigger = 1
                }
                if(trigger == 0){
                    finalMovies.remove(movies[i])
                    deleteMovieIntoDatabase(context.context, movies[i])
                }
            }

            for (i in 0 until finalMovies.size){
                for (j in 0 until favoriteList.size){
                    if (finalMovies[i].id == favoriteList[j].id) {
                        finalMovies[i].favorite = true
                    }
                }
            }

            if (finalMovies.size != 0){
                movies.clear()
                movies.addAll(finalMovies)
            }
        }
    }

    private fun loadingMovies(): Array<Movie> { //load movies from the api
        val url = URL("http://$SERVER:8888/user/$GETMOVIES.php?user=$USER&pass=$PASSWORD")
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConnection.connectTimeout = 2500
        urlConnection.readTimeout = 2500
        try {
            val input: InputStream = BufferedInputStream(urlConnection.inputStream)
            val response = readStream(input)
            return Gson().fromJson(response, Array<Movie>::class.java)
        }catch (e: Exception){}
        finally {
            urlConnection.disconnect()
        }
        return arrayOf()
    }

    private fun insertMoviesToDatabase(){ //inserting all movies to the database
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context.context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        for (i in 0 until movies().size){
            val row = ContentValues()
            row.put(IDMOVIE, movies[i].id)
            row.put(TITLE, movies[i].title)
            row.put(GENRES, transformGenresArrayListToString(i))
            row.put(DESCRIPTION, movies[i].description)
            row.put(DIRECTOR, movies[i].director)
            row.put(ACTORS, transformActorsArrayListToString(i))
            row.put(YEAR, movies[i].year)
            row.put(RUNTIME, movies[i].runtime)
            row.put(RATING, movies[i].rating)
            row.put(VOTES, movies[i].votes)
            row.put(REVENUE,movies[i].revenue)
            row.put(FAVORITE, movies[i].favorite)
            dataBase.insert(TABLEMOVIES, null, row)
        }
        dataBase.close()
    }

    private fun insertMovieToDatabase(movie: Movie){ //inserting only one movie
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context.context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        val row = ContentValues()
        row.put(IDMOVIE, movie.id)
        row.put(TITLE, movie.title)
        row.put(GENRES, transformGenresArrayListToStringOneMovie(movie))
        row.put(DESCRIPTION, movie.description)
        row.put(DIRECTOR, movie.director)
        row.put(ACTORS, transformActorsArrayListToStringOneMovie(movie))
        row.put(YEAR, movie.year)
        row.put(RUNTIME, movie.runtime)
        row.put(RATING, movie.rating)
        row.put(VOTES, movie.votes)
        row.put(REVENUE,movie.revenue)
        row.put(FAVORITE, movie.favorite)
        dataBase.insert(TABLEMOVIES, null, row)

        dataBase.close()
    }
/* For speed and accesibility reasons i convert the int arrays in strings so i have functions to transform these data in both ways
*/

    private fun transformGenresArrayListToString(pos: Int): String{
        var result = ""
        for (i in 0 until movies[pos].genres!!.size - 1){
            result += "${movies[pos].genres!![i]},"
        }
        result += movies[pos].genres!![movies[pos].genres!!.size - 1]
        return result
    }

    private fun transformActorsArrayListToString(pos: Int): String{
        var result = ""
        for (i in 0 until movies[pos].actors!!.size - 1){
            result += "${movies[pos].actors!![i]},"
        }
        result += movies[pos].actors!![movies[pos].actors!!.size - 1]
        return result
    }

    private fun searchMovies(): ArrayList<Movie>{ //getting movies from database
        //i design a system for getting always the next record on the table
        var movieResult: Movie? = null
        val moviesSize = getTableSize(context.context, TABLEMOVIES)
        var minIndex = selectMinIndex(context.context, "$IDMOVIE", "$TABLEMOVIES")
        sortTablesById(context.context, "$IDMOVIE", "$TABLEMOVIES")
        for (i in 0 until moviesSize){
            val movie = Movie(0, "", null, "", "", null, 0, 0, 0f, 0, 0f, false)
            do{
                movieResult = searchInTableMovie(movie, minIndex)
                minIndex += 1
            }while (movieResult?.title == "" || movieResult?.director == "")
            if (movieResult != null) {
                movies.add(movieResult)
            }
        }
        return movies
    }

    private fun searchInTableMovie(movie: Movie, pos: Int): Movie{ //search a specific movie

        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context.context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase
        lateinit var cursorMovieData: Cursor
        try {
            cursorMovieData = dataBase.rawQuery("select * from $TABLEMOVIES where $IDMOVIE=${pos}", null)
            if(cursorMovieData != null && cursorMovieData.moveToFirst()) {
                movie.id = cursorMovieData.getString(0).toInt()
                movie.title = cursorMovieData.getString(1)
                movie.genres = transformGenresStringToArray(cursorMovieData.getString(2))
                movie.description = cursorMovieData.getString(3)
                movie.director = cursorMovieData.getString(4)
                movie.actors = transformActorsStringToArray(cursorMovieData.getString(5))
                movie.year = cursorMovieData.getString(6).toInt()
                movie.runtime = cursorMovieData.getString(7).toInt()
                movie.rating = cursorMovieData.getString(8).toFloat()
                movie.votes = cursorMovieData.getString(9).toInt()
                movie.revenue = cursorMovieData.getString(10).toFloat()
                var fav = cursorMovieData.getString(11)
                movie.favorite = fav.toInt() == 1
            }
        }finally{cursorMovieData.close()}
        dataBase.close()
        return movie
    }

    private fun transformGenresStringToArray(genresString: String): ArrayList<Int>{
        val genresArray = genresString.split(",")
        val array = arrayListOf<Int>()
        for(element in genresArray){
            array.add(element.toInt())
        }
        return  array
    }

    private fun transformActorsStringToArray(actorsString: String): ArrayList<Int>{
        val actorsArray = actorsString.split(",")
        val array = arrayListOf<Int>()
        for(element in actorsArray){
            if (element != "")
                array.add(element.toInt())
        }
        return  array
    }
}

private fun readStream(inputStream: InputStream): String{
    val br = BufferedReader(InputStreamReader(inputStream))
    val total = StringBuilder()
    while (true){
        val line = br.readLine() ?: break
        total.append(line).append(('\n'))
    }
    return total.toString()
}