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
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Genre
import es.usj.androidapps.alu100485.movieslibrary.singlenton.GenresSingleton
import es.usj.androidapps.alu100485.movieslibrary.sqlite.AdminSQLiteOpenHelper
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//This class is seemed to the Load movies
class LoadGenresAsync(val context: LoadDataAsync): AsyncTask<Void, Int, ArrayList<Genre>>() {
    var genres: ArrayList<Genre> = arrayListOf()
    var databaseAvailable = checkDatabaseAvailable(context.context, TABLEACTORS)

    override fun doInBackground(vararg p0: Void?): ArrayList<Genre> {

        if (databaseAvailable) {
            genres = searchGenres()
            val genresFromApiFetch = arrayListOf<Genre>()
            loadingGenres().toCollection(genresFromApiFetch)
            fetchGenres(genresFromApiFetch)
        } else
            loadingGenres().toCollection(genres)

        return  genres
    }

    override fun onPostExecute(result: ArrayList<Genre>) {
        super.onPostExecute(result)

        GenresSingleton.genres.addAll(genres)
        if(!databaseAvailable)
            insertGenresToDatabase()
    }

    private fun fetchGenres(genresFetch: ArrayList<Genre>){

        val finalGenres = arrayListOf<Genre>()
        if(genresFetch.size != 0){
            genres.forEach {
                finalGenres.add(it)
            }

            genresFetch.forEach {
                if (genres.contains(it)) {
                    updateGenreIntoDatabase(context.context, it)
                }else {
                    finalGenres.add(it)
                    insertGenreToDatabase(it)
                }
            }

            genres.forEach {
                if (!genresFetch.contains(it)) {
                    finalGenres.remove(it)
                    deleteGenreIntoDatabase(context.context, it)
                }
            }

            if (finalGenres.size != 0){
                genres.clear()
                genres.addAll(finalGenres)
            }
        }
    }

    private fun deleteGenreIntoDatabase(context: Context, genre: Genre){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        dataBase.delete(TABLEGENRES, "$IDGENRE=${genre.id}", null)

        dataBase.close()
    }

    private fun loadingGenres(): Array<Genre> {
        val url = URL("http://$SERVER:8888/user/$GETGENRES.php?user=$USER&pass=$PASSWORD")
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConnection.connectTimeout = 2500
        urlConnection.readTimeout = 2500
        try {
            val input: InputStream = BufferedInputStream(urlConnection.inputStream)
            val response = readStream(input)
            return Gson().fromJson(response, Array<Genre>::class.java)
        }catch (e: Exception){
            showError()
            //Do SOMETHING
        }finally {
            urlConnection.disconnect()
        }
        return arrayOf()
    }

    private fun insertGenresToDatabase(){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context.context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        for (i in 0 until genres().size){
            val row = ContentValues()
            row.put(IDGENRE, genres[i].id)
            row.put(NAMEGENRE, genres[i].name)
            dataBase.insert(TABLEGENRES, null, row)
        }
        dataBase.close()
    }

    private fun insertGenreToDatabase(genre: Genre){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context.context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        for (i in 0 until genres().size){
            val row = ContentValues()
            row.put(IDGENRE, genre.id)
            row.put(NAMEGENRE, genre.name)
            dataBase.insert(TABLEGENRES, null, row)
        }
        dataBase.close()
    }

    private fun updateGenreIntoDatabase(context: Context, genre: Genre){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        val row = ContentValues()
        row.put(IDGENRE, genre.id)
        row.put(NAMEGENRE, genre.name)
        dataBase.update(TABLEGENRES, row, "$IDGENRE=${genre.id}", null)

        dataBase.close()
    }

    private fun searchGenres(): ArrayList<Genre>{
        lateinit var genreResult: Genre
        val genresSize = getTableSize(context.context, TABLEGENRES)
        var minIndex = selectMinIndex(context.context, IDGENRE, TABLEGENRES)
        sortTablesById(context.context, IDGENRE, TABLEGENRES)

        for (i in 0 until genresSize){
            do{
                genreResult = searInTableGenres(minIndex)
                minIndex += 1
            }while (genreResult?.id == -1 || genreResult.name == "")
            genres.add(genreResult)
        }
        return genres
    }

    private fun searInTableGenres(pos: Int): Genre{
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context.context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase
        lateinit var cursor: Cursor
        var genre = Genre(-1, "")
        try {
            cursor = dataBase.rawQuery("select * from $TABLEGENRES where $IDGENRE=${pos}", null)
            if(cursor != null && cursor.moveToFirst())
                genre =  Genre(cursor.getString(0).toInt(), cursor.getString(1))
        }finally {cursor.close()
        dataBase.close()}

        return genre
    }

    private fun showError(){
        //Toast.makeText(context, "Fail connection", Toast.LENGTH_SHORT).show() hacer delegate
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