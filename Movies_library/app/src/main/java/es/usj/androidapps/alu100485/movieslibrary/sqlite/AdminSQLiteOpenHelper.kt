package es.usj.androidapps.alu100485.movieslibrary.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import es.usj.androidapps.alu100485.movieslibrary.constants.*
//Creating the database and their tables
class AdminSQLiteOpenHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $TABLEMOVIES ($IDMOVIE int primary key, $TITLE text, $GENRES text, $DESCRIPTION text, $DIRECTOR text, $ACTORS text, $YEAR int, $RUNTIME int, $RATING real, $VOTES int, $REVENUE real, $FAVORITE boolean)")
        db?.execSQL("create table $TABLEACTORS ($IDACTOR int primary key, $NAMEACTOR text)")
        db?.execSQL("create table $TABLEGENRES ($IDGENRE int primary key, $NAMEGENRE text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}