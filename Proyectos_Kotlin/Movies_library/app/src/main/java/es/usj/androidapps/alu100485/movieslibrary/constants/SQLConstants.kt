package es.usj.androidapps.alu100485.movieslibrary.constants

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import es.usj.androidapps.alu100485.movieslibrary.sqlite.AdminSQLiteOpenHelper

const val DATABASE = "database"

const val TABLEMOVIES = "movies"
const val TABLEACTORS = "actors"
const val TABLEGENRES = "genres"

const val IDMOVIE = "idMovie"
const val TITLE = "title"
const val GENRES = "genres"
const val DESCRIPTION = "description"
const val DIRECTOR = "director"
const val ACTORS = "actors"
const val YEAR = "year"
const val RUNTIME = "runtime"
const val RATING = "rating"
const val VOTES = "votes"
const val REVENUE = "revenue"
const val FAVORITE = "favorite"

const val IDACTOR = "idActor"
const val NAMEACTOR = "nameActor"

const val IDGENRE = "idGenre"
const val NAMEGENRE = "nameGenre"


fun checkDatabaseAvailable(context: Context, table: String): Boolean{ //checking if the database has items
    val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context, DATABASE, null, 1)
    val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.readableDatabase
    val cursor: Cursor = dataBase.rawQuery("select count(*) from $table", null)
    cursor.moveToFirst()
    val count = cursor.getInt(0)
    cursor.close()
    if (count > 0) {
        dataBase.close()
        return true
    }
    dataBase.close()
    return false
}

fun getTableSize(context: Context, table: String): Int{
    val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context, DATABASE, null, 1)
    val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

    val cursorCount = dataBase.rawQuery("select count(*) from $table", null)
    cursorCount.moveToFirst()
    val count = cursorCount.getInt(0)
    cursorCount.close()
    return count
}

fun updateMovieIntoDatabase(context: Context, movie: Movie){
    val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context, DATABASE, null, 1)
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
    dataBase.update(TABLEMOVIES, row, "$IDMOVIE=${movie.id}", null)

    dataBase.close()
}

fun transformGenresArrayListToStringOneMovie(movie: Movie): String{
    var result = ""

    if (movie.genres != null) {
        for (i in 0 until movie.genres!!.size - 1) {
            result += "${movie.genres!![i]},"
        }
        result += movie.genres!![movie.genres!!.size - 1]
    }
    return result
}

fun transformActorsArrayListToStringOneMovie(movie: Movie): String{
    var result = ""
    if (movie.actors != null) {
        for (i in 0 until movie.actors!!.size - 1) {
            result += "${movie.actors!![i]},"
        }
        var a = 0
        //if(movie.id == 1001)
            //a = 0
        result += movie.actors!![movie.actors!!.size - 1]
    }
    return result
}

fun deleteMovieIntoDatabase(context: Context, movie: Movie){
    val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context, DATABASE, null, 1)
    val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

    dataBase.delete(TABLEMOVIES, "$IDMOVIE=${movie.id}", null)

    dataBase.close()
}

fun selectMinIndex(context: Context, id: String, table: String): Int{ //this is for selecting the min id in the table
    val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context, DATABASE, null, 1)
    val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

    val cursor = dataBase.rawQuery("Select min($id) from $table", null)
    cursor.moveToFirst()
    val pos = cursor.getString(0)
    dataBase.close()
    return pos.toInt()
}

fun sortTablesById(context: Context, id: String, table: String){
    val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context, DATABASE, null, 1)
    val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase
    dataBase.rawQuery("Select * from $table ORDER BY $id ASC", null)
    dataBase.close()
}