package es.usj.androidapps.alu100485.movieslibrary

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import es.usj.androidapps.alu100485.movieslibrary.asynctask.post.PostActorAsyncTask
import es.usj.androidapps.alu100485.movieslibrary.asynctask.post.PostGenreAsyncTask
import es.usj.androidapps.alu100485.movieslibrary.asynctask.post.PostMovieAsyncTask
import es.usj.androidapps.alu100485.movieslibrary.asynctask.update.UpdateActorAsyncTask
import es.usj.androidapps.alu100485.movieslibrary.asynctask.update.UpdateGenreAsyncTask
import es.usj.androidapps.alu100485.movieslibrary.constants.*
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Actor
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Genre
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import es.usj.androidapps.alu100485.movieslibrary.singlenton.ActorsSingleton
import es.usj.androidapps.alu100485.movieslibrary.singlenton.GenresSingleton
import es.usj.androidapps.alu100485.movieslibrary.singlenton.MoviesSingleton
import es.usj.androidapps.alu100485.movieslibrary.sqlite.AdminSQLiteOpenHelper
import kotlinx.android.synthetic.main.activity_add_movie.*
import java.lang.Exception
import java.util.*
//This activity and edit movie are seemed
class AddMovie : AppCompatActivity() {

    var id = getNewId()
    var actorsList: ArrayList<Actor> = arrayListOf()
    var genresList: ArrayList<Genre> = arrayListOf()
    var movie: Movie = Movie(id, "", arrayListOf(), "", "", arrayListOf(), 0, 0, 0f, 0, 0f, false)
    var checkingYear = false
    var checkingRuntime = false
    var checkingRating = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_movie)

        checkYear()
        checkRuntime()
        checkRating()

        etIdAddMovie.setText("$id")
        btnCreateListener()
    }

    private fun getNewId(): Int{ //get new id for the new movie
        var id = MoviesSingleton.movies.size
        for (i in 0 until MoviesSingleton.movies.size){
            if(id < MoviesSingleton.movies[i].id)
                id = MoviesSingleton.movies[i].id
        }
        id += 1
        return id
    }

    private fun btnCreateListener(){
        btnCreateMovie.setOnClickListener {
            if(checkData()){
                btnCreateMovie.isClickable = false
                MoviesSingleton.movies.add(movie)
                PostMovieAsyncTask().execute(movie) //post request
                insertMovieIntoDatabase() //inserting the movie in database
                finish() // i finish and i go back to the previous activity
            }else
                Toast.makeText(this, "Error! Check the introduced data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkData(): Boolean{
        if (etTitleAddMovie.text.isNullOrEmpty() || etDirectorAddMovie.text.isNullOrEmpty()
                || etDescriptionContentAddMovie.text.isNullOrEmpty() || checkingYear
                || checkingRuntime || checkingRating
                || etVotesAddMovie.text.isNullOrEmpty() || etRevenueAddMovie.text.isNullOrEmpty()
                || etActorsListAddMovie.text.isNullOrEmpty() || etGenresListAddMovie.text.isNullOrEmpty()) {
            return false
        } else {
            movie.title = etTitleAddMovie.text.toString()
            movie.director = etDirectorAddMovie.text.toString()
            movie.description = etDescriptionContentAddMovie.text.toString()
            movie.year = etYearAddMovie.text.toString().toInt()
            movie.runtime = etRuntimeAddMovie.text.toString().toInt()
            movie.rating = etRatingAddMovie.text.toString().toFloat()
            movie.votes = etVotesAddMovie.text.toString().toInt()
            movie.revenue = etRevenueAddMovie.text.toString().toFloat()
            transformToArray()
            return true
        }
    }

    private fun transformToArray(){ //transformations between strings and array and inside out
        val actorsText = etActorsListAddMovie.text
        val actors = actorsText.split(",")

        val genresText = etGenresListAddMovie.text
        val genres = genresText.split(",")

        for (i in actors.indices){
            if(actorExist(actors[i])){
                actorsList.add(i, Actor(getActorId(actors[i]), actors[i]))
                updateActorsIntoDatabase(Actor(getActorId(actors[i]), actors[i]))
                UpdateActorAsyncTask().execute(Actor(getActorId(actors[i]), actors[i]))
            }else {
                actorsList.add(i, Actor(actors().size + 1, actors[i]))
                ActorsSingleton.actors.add(Actor(actors().size + 1, actors[i]))
                PostActorAsyncTask().execute(Actor(actors().size, actors[i]))
                insertActorIntoDatabase(Actor(actors().size , actors[i]))
            }
            movie.actors!!.add(actorsList[i].id)
        }

        for (i in genres.indices){
            if(genreExist(genres[i])){
                genresList.add(i, Genre(getGenreId(genres[i]), genres[i]))
                updateGenresIntoDatabase(Genre(getGenreId(genres[i]), genres[i]))
                UpdateGenreAsyncTask().execute(Genre(getGenreId(genres[i]), genres[i]))
            }else {
                genresList.add(i, Genre(genres().size + 1, genres[i]))
                GenresSingleton.genres.add(Genre(GenresSingleton.genres.size + 1, genres[i]))
                PostGenreAsyncTask().execute(Genre(genres().size, genres[i]))
                insertGenreIntoDatabase(Genre(genres().size, genres[i]))
            }
            movie.genres!!.add(genresList[i].id)
        }
    }

    private fun actorExist(name: String): Boolean{ //checing if the actor exist or this is new
        for (i in actors().indices){
            if(actors()[i].name == name)
                return true
        }
    return false
    }

    private fun getActorId(name: String): Int{ //if exist get its id
        for (i in actors().indices){
            if(actors()[i].name == name)
                return actors()[i].id
        }
        return -1
    }

    private fun genreExist(name: String): Boolean{
        for (i in genres().indices){
            if(genres()[i].name == name)
                return true
        }
        return false
    }

    private fun getGenreId(name: String): Int{
        for (i in genres().indices){
            if(genres()[i].name == name)
                return genres()[i].id
        }
        return -1
    }

    private fun checkYear(){
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"))
        calendar.time = Date()
        etYearAddMovie.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    checkingYear = if (etYearAddMovie.text.isNullOrEmpty() || etYearAddMovie.text.toString().toInt() > calendar[Calendar.YEAR] || etYearAddMovie.text.toString().toInt() < 1900) {
                        Toast.makeText(applicationContext, "Fill the field or enter a valid year", Toast.LENGTH_SHORT).show()
                        true
                    } else false
                }catch (e: Exception){}
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun checkRuntime(){
        etRuntimeAddMovie.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    checkingRuntime = if (etRuntimeAddMovie.text.isNullOrEmpty() || etRuntimeAddMovie.text.toString().toInt() > 300) {
                        Toast.makeText(applicationContext, "Fill the field or enter a valid length", Toast.LENGTH_SHORT).show()
                        true
                    } else false
                }catch (e: Exception){}
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun checkRating(){
        etRatingAddMovie.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    checkingRating = if (etRatingAddMovie.text.isNullOrEmpty() || etRatingAddMovie.text.toString().toFloat() > 10.0) {
                        Toast.makeText(applicationContext, "Fill the field or enter a valid rate", Toast.LENGTH_SHORT).show()
                        true
                    } else false
                }catch (e: Exception){}
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun insertMovieIntoDatabase(){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(applicationContext, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        val row = ContentValues()
        row.put(IDMOVIE, movie.id)
        row.put(TITLE, movie.title)
        row.put(GENRES, transformGenresToString())
        row.put(DESCRIPTION, movie.description)
        row.put(DIRECTOR, movie.director)
        row.put(ACTORS, transformActorsToString())
        row.put(YEAR, movie.year)
        row.put(RUNTIME, movie.runtime)
        row.put(RATING, movie.rating)
        row.put(VOTES, movie.votes)
        row.put(REVENUE,movie.revenue)
        row.put(FAVORITE, movie.favorite)
        dataBase.insert(TABLEMOVIES, null, row)

        dataBase.close()
    }

    private fun insertActorIntoDatabase(actor: Actor){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(applicationContext, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        val row = ContentValues()
        row.put(IDACTOR, actor.id)
        row.put(NAMEACTOR, actor.name)

        dataBase.insert(TABLEACTORS, null, row)
        dataBase.close()
    }

    private fun insertGenreIntoDatabase(genre: Genre){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(applicationContext, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        val row = ContentValues()
        row.put(IDGENRE, genre.id)
        row.put(NAMEGENRE, genre.name)

        dataBase.insert(TABLEGENRES, null, row)
        dataBase.close()
    }

    private fun transformGenresToString(): String{
        var result = ""
        for (i in 0 until movie.genres!!.size - 1){
            result += "${movie.genres!![i]},"
        }
        result += movie.genres!![movie.genres!!.size - 1]
        return result
    }

    private fun transformActorsToString(): String{
        var result = ""
        for (i in 0 until movie.actors!!.size - 1){
            result += "${movie.actors!![i]},"
        }
        result += movie.actors!![movie.actors!!.size - 1]
        return result
    }

    private fun updateActorsIntoDatabase(actor: Actor){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(applicationContext, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        val row = ContentValues()
        row.put(IDACTOR, actor.id)
        row.put(NAMEACTOR, actor.name)

        dataBase.update(TABLEACTORS, row, "$IDACTOR=${actor.id}", null)
        dataBase.close()
    }

    private fun updateGenresIntoDatabase(genre: Genre){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(applicationContext, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        val row = ContentValues()
        row.put(IDGENRE, genre.id)
        row.put(NAMEGENRE, genre.name)

        dataBase.update(TABLEGENRES, row, "$IDGENRE=${genre.id}", null)
        dataBase.close()
    }
}