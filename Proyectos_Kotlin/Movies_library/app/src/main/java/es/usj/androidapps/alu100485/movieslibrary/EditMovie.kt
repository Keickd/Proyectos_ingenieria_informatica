package es.usj.androidapps.alu100485.movieslibrary

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import es.usj.androidapps.alu100485.movieslibrary.asynctask.post.PostActorAsyncTask
import es.usj.androidapps.alu100485.movieslibrary.asynctask.post.PostGenreAsyncTask
import es.usj.androidapps.alu100485.movieslibrary.asynctask.update.UpdateActorAsyncTask
import es.usj.androidapps.alu100485.movieslibrary.asynctask.update.UpdateGenreAsyncTask
import es.usj.androidapps.alu100485.movieslibrary.asynctask.update.UpdateMovieAsyncTask
import es.usj.androidapps.alu100485.movieslibrary.constants.*
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Actor
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Genre
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import es.usj.androidapps.alu100485.movieslibrary.singlenton.ActorsSingleton
import es.usj.androidapps.alu100485.movieslibrary.singlenton.GenresSingleton
import es.usj.androidapps.alu100485.movieslibrary.singlenton.MoviesSingleton
import es.usj.androidapps.alu100485.movieslibrary.sqlite.AdminSQLiteOpenHelper
import kotlinx.android.synthetic.main.activity_add_movie.*
import kotlinx.android.synthetic.main.activity_edit_movie.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class EditMovie : AppCompatActivity() {

    lateinit var movie: Movie
    private var actorsArray: ArrayList<Actor> = arrayListOf()
    private var genresArray: ArrayList<Genre> = arrayListOf()
    var actorsNames: ArrayList<String> = arrayListOf()
    var genresNames: ArrayList<String> = arrayListOf()
    var checkingYear = false
    var checkingRuntime = false
    var checkingRating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_movie)
        settingsScrolls()
        movie = intent.getParcelableExtra("movieEditInfo")
        loadImageForViews(applicationContext, movie, ivCoverEditMovie)
        checkYear()
        checkRuntime()
        checkRating()
        manageFavorites(movie, ibtnFavoriteEditMovie, applicationContext)
        fulfillEditMovieInfo()
        manageSaveButton()
    }

    private fun getNames(){
        for(i in 0 until ActorsSingleton.actors.size){
            for(j in 0 until movie.actors!!.size){
                if(ActorsSingleton.actors[i].id == movie.actors!![j])
                    actorsNames.add(ActorsSingleton.actors[i].name)
            }
        }

        for(i in 0 until GenresSingleton.genres.size){
            for(j in 0 until movie.genres!!.size){
                if(ActorsSingleton.actors[i].id == movie.genres!![j])
                    genresNames.add(GenresSingleton.genres[i].name)
            }
        }
    }

    private fun settingsScrolls(){
        etActorsListEditMovie.movementMethod = ScrollingMovementMethod()
        etGenresListEditMovie.movementMethod = ScrollingMovementMethod()
    }

    private fun fulfillEditMovieInfo(){
        etId.setText(movie.id.toString())
        etTitle.setText(movie.title)
        etDescriptionContent.setText(movie.description)
        etDirector.setText(movie.director)
        etYear.setText(movie.year.toString())
        etRuntime.setText(movie.runtime.toString())
        etRating.setText(movie.rating.toString())
        etVotes.setText(movie.votes.toString())
        etRevenue.setText(movie.revenue.toString())
        getNames()
        etActorsListEditMovie.setText(fulfillActors())
        etGenresListEditMovie.setText(fulfillGenres())
    }

    private fun fulfillActors(): String{
        var names = ""
        for (i in 0 until actorsNames.size - 1){
            names += "${actorsNames[i]},"
        }
        names += actorsNames[actorsNames.size - 1]
        return names
    }

    private fun fulfillGenres(): String{
        var names = ""
        for (i in 0 until genresNames.size - 1){
            names += "${genresNames[i]},"
        }
        names += genresNames[genresNames.size - 1]
        return names
    }

    private fun manageSaveButton(){
        btnSave.setOnClickListener {
            if(checkData()){
                btnSave.isClickable = false
                UpdateMovieAsyncTask().execute(movie)
                updateMoviesSingleton()
                updateMovieIntoDatabase()
                finish()
            }else
                Toast.makeText(this, "Error! Check the introduced data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMoviesSingleton(){
        for (i in 0 until MoviesSingleton.movies.size){
            if (MoviesSingleton.movies[i].id == movie.id)
                MoviesSingleton.movies[i] = movie
        }
    }

    private fun checkData(): Boolean{
        if (etTitle.text.isNullOrEmpty() || etDirector.text.isNullOrEmpty()
                || etDescriptionContent.text.isNullOrEmpty() || checkingYear
                || checkingRuntime || checkingRating
                || etVotes.text.isNullOrEmpty() || etRevenue.text.isNullOrEmpty()
                || etActorsListEditMovie.text.isNullOrEmpty() || etGenresListEditMovie.text.isNullOrEmpty()) {
            return false
        } else {
            movie.title = etTitle.text.toString()
            movie.director = etDirector.text.toString()
            movie.description = etDescriptionContent.text.toString()
            movie.year = etYear.text.toString().toInt()
            movie.runtime = etRuntime.text.toString().toInt()
            movie.rating = etRating.text.toString().toFloat()
            movie.votes = etVotes.text.toString().toInt()
            movie.revenue = etRevenue.text.toString().toFloat()
            transformToArray()
            return true
        }
    }

    private fun  transformToArray(){
        val actorsText = etActorsListEditMovie.text
        val actors = actorsText.split(",")

        val genresText = etGenresListEditMovie.text
        val genres = genresText.split(",")
        movie.actors!!.clear()
        movie.genres!!.clear()
        for (i in actors.indices){
            if(actorExist(actors[i])){
                actorsArray.add(i, Actor(getActorId(actors[i]), actors[i]))
                UpdateActorAsyncTask().execute(Actor(getActorId(actors[i]), actors[i]))
                updateActorsIntoDatabase(Actor(getActorId(actors[i]), actors[i]))
            }else {
                actorsArray.add(i, Actor(actors().size + 1, actors[i]))
                ActorsSingleton.actors.add(Actor(actors().size + 1, actors[i]))
                PostActorAsyncTask().execute(Actor(actors().size, actors[i]))
                insertActorIntoDatabase(Actor(actors().size , actors[i])) //pq la base de datos empieza en 1
            }
            movie.actors!!.add(actorsArray[i].id)
        }

        for (i in genres.indices){
            if(genreExist(genres[i])){
                genresArray.add(i, Genre(getGenreId(genres[i]), genres[i]))
                UpdateGenreAsyncTask().execute(Genre(getGenreId(genres[i]), genres[i]))
                updateGenresIntoDatabase(Genre(getGenreId(genres[i]), genres[i]))
            }else {
                genresArray.add(i, Genre(genres().size + 1, genres[i]))
                GenresSingleton.genres.add(Genre(GenresSingleton.genres.size + 1, genres[i]))
                PostGenreAsyncTask().execute(Genre(genres().size, genres[i]))
                insertGenreIntoDatabase(Genre(genres().size, genres[i]))
            }
            movie.genres!!.add(genresArray[i].id)
        }
    }

    private fun actorExist(name: String): Boolean{
        for (i in actors().indices){
            if(actors()[i].name == name)
                return true
        }
        return false
    }

    private fun getActorId(name: String): Int{
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
        etYear.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    checkingYear = if (etYear.text.isNullOrEmpty() || etYear.text.toString().toInt() > calendar[Calendar.YEAR] || etYear.text.toString().toInt() < 1900) {
                        Toast.makeText(applicationContext, "Fill the field or enter a valid year", Toast.LENGTH_SHORT).show()
                        true
                    } else false
                }catch (e: Exception){}
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun checkRuntime(){
        etRuntime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    checkingRuntime = if (etRuntime.text.isNullOrEmpty() || etRuntime.text.toString().toInt() > 300) {
                        Toast.makeText(applicationContext, "Fill the field or enter a valid length", Toast.LENGTH_SHORT).show()
                        true
                    } else false
                }catch (e: Exception){}
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun checkRating(){
        etRating.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    checkingRating = if (etRating.text.isNullOrEmpty() || etRating.text.toString().toFloat() > 10.0) {
                        Toast.makeText(applicationContext, "Fill the field or enter a valid rate", Toast.LENGTH_SHORT).show()
                        true
                    } else false
                }catch (e: Exception){}
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateMovieIntoDatabase(){
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
        dataBase.update(TABLEMOVIES, row, "$IDMOVIE=${movie.id}", null)

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

    private fun updateActorsIntoDatabase(actor: Actor){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(applicationContext, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        val row = ContentValues()
        row.put(IDACTOR, actor.id)
        row.put(NAMEACTOR, actor.name)

        dataBase.update(TABLEACTORS, row, "$IDACTOR=${actor.id}", null)
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

    private fun updateGenresIntoDatabase(genre: Genre){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(applicationContext, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        val row = ContentValues()
        row.put(IDGENRE, genre.id)
        row.put(NAMEGENRE, genre.name)

        dataBase.update(TABLEGENRES, row, "$IDGENRE=${genre.id}", null)
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
}