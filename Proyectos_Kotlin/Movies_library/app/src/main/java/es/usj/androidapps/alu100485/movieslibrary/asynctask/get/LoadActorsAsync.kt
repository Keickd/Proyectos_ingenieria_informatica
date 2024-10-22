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
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Actor
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Genre
import es.usj.androidapps.alu100485.movieslibrary.singlenton.ActorsSingleton
import es.usj.androidapps.alu100485.movieslibrary.sqlite.AdminSQLiteOpenHelper
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

//This class is seemed to the LoadMovies
class LoadActorsAsync(val context: LoadDataAsync): AsyncTask<Void, Int, ArrayList<Actor>>() {
    var actors: ArrayList<Actor> = arrayListOf()
    var databaseAvailable = checkDatabaseAvailable(context.context, "$TABLEACTORS")
    override fun doInBackground(vararg p0: Void?): ArrayList<Actor> {

        if (databaseAvailable) {
            actors = searchActors()
            val actorsFromApiFetch = arrayListOf<Actor>()
            loadingActors().toCollection(actorsFromApiFetch)
            fetchActors(actorsFromApiFetch)
        }else
            loadingActors().toCollection(actors)

        return actors
    }

    override fun onPostExecute(result: ArrayList<Actor>) {
        super.onPostExecute(result)

        ActorsSingleton.actors.addAll(actors)
        if(!databaseAvailable)
            insertActorsToDatabase()

    }

    private fun loadingActors(): Array<Actor> {
        val url = URL("http://$SERVER:8888/user/$GETACTORS.php?user=$USER&pass=$PASSWORD")
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        urlConnection.connectTimeout = 2500
        urlConnection.readTimeout = 2500
        try {
            val input: InputStream = BufferedInputStream(urlConnection.inputStream)
            val response = readStream(input)
            return Gson().fromJson(response, Array<Actor>::class.java)
        }catch (e: Exception){}finally {
            urlConnection.disconnect()
        }
        return arrayOf()
    }

    private fun insertActorsToDatabase(){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context.context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        for (i in 0 until actors().size){
            val row = ContentValues()
            row.put(IDACTOR, actors[i].id)
            row.put(NAMEACTOR, actors[i].name)
            dataBase.insert(TABLEACTORS, null, row)
        }
        dataBase.close()
    }

    private fun searchActors(): ArrayList<Actor>{
        lateinit var actorResult: Actor
        val actorsSize = getTableSize(context.context, TABLEACTORS)
        var minIndex = selectMinIndex(context.context, IDACTOR, TABLEACTORS)
        sortTablesById(context.context, IDGENRE, TABLEGENRES)

        for (i in 0 until actorsSize){
            do{
                actorResult = searInTableActors(minIndex)
                minIndex += 1
            }while (actorResult?.id == -1 || actorResult.name == "")
            actors.add(actorResult)
        }
        return actors
    }

    private fun fetchActors(actorsFetch: ArrayList<Actor>){

        val finalActors = arrayListOf<Actor>()
        if(actorsFetch.size != 0){
            actors.forEach {
                finalActors.add(it)
            }

            actorsFetch.forEach {
                if (actors.contains(it)) {
                    updateActorIntoDatabase(context.context, it)
                }else {
                    finalActors.add(it)
                    insertActorToDatabase(it)
                }
            }

            actors.forEach {
                if (!actorsFetch.contains(it)) {
                    finalActors.remove(it)
                    deleteActorIntoDatabase(context.context, it)
                }
            }

            if (finalActors.size != 0){
                actors.clear()
                actors.addAll(finalActors)
            }
        }
    }

    private fun deleteActorIntoDatabase(context: Context, actor: Actor){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        dataBase.delete(TABLEACTORS, "$IDACTOR=${actor.id}", null)

        dataBase.close()
    }

    private fun updateActorIntoDatabase(context: Context, actor: Actor){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        val row = ContentValues()
        row.put(IDACTOR, actor.id)
        row.put(NAMEACTOR, actor.name)
        dataBase.update(TABLEACTORS, row, "$IDACTOR=${actor.id}", null)

        dataBase.close()
    }

    private fun insertActorToDatabase(actor: Actor){
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context.context, DATABASE, null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase

        for (i in 0 until actors().size){
            val row = ContentValues()
            row.put(IDACTOR, actor.id)
            row.put(NAMEACTOR, actor.name)
            dataBase.insert(TABLEACTORS, null, row)
        }
        dataBase.close()
    }

    private fun searInTableActors(pos: Int): Actor{
        val adminSQLiteOpenHelper = AdminSQLiteOpenHelper(context.context, "$DATABASE", null, 1)
        val dataBase: SQLiteDatabase = adminSQLiteOpenHelper.writableDatabase
        lateinit var cursor: Cursor
        var actor = Actor(-1, "")
        try {
            cursor = dataBase.rawQuery("select * from $TABLEACTORS where $IDACTOR=${pos}", null)
            if(cursor != null && cursor.moveToFirst())
                actor = Actor(cursor.getString(0).toInt(), cursor.getString(1))
        }finally {cursor.close()}

        return actor
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

