package es.usj.androidapps.alu100485.movieslibrary.asynctask.get

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.view.View
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import es.usj.androidapps.alu100485.movieslibrary.R
import es.usj.androidapps.alu100485.movieslibrary.constants.APIKEY
import es.usj.androidapps.alu100485.movieslibrary.constants.checkDownloadedCover
import es.usj.androidapps.alu100485.movieslibrary.constants.loadImageForAdapters
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import es.usj.androidapps.alu100485.movieslibrary.dataclass.MovieOMDB
import kotlinx.android.synthetic.main.item_movie.view.*
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class LoadMovieCover(@SuppressLint("StaticFieldLeak") val itemView: View, private val movie: Movie): AsyncTask<Void, Int, Void>() {

    var movieOMDB: MovieOMDB? = null

    override fun doInBackground(vararg params: Void?): Void? {

        val cm = itemView.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true

        if(!checkDownloadedCover(itemView, movie) && isConnected) { //for checking internet conection, i only save image if there is internet
            movieOMDB = loadAPICovers(movie.title)
            saveImage()
        }else
            movieOMDB = null
        return null
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        itemView.ivMovie.setImageResource(R.drawable.no_image) //at the beginning there is no image
        if(movieOMDB?.Poster != null)
            Picasso.get().load(movieOMDB!!.Poster).into(itemView.ivMovie) //downloading the image and putting it in the imageview
        else
            loadImageForAdapters(itemView, movie) //if it is downloaded i load it form memory

    }

    private fun saveImage(){
        var image: Bitmap? = null
        try {
            image = Picasso.get().load("${movieOMDB?.Poster?.trim()}").get()
            if(image == null)
                itemView.ivMovie.setImageResource(R.drawable.no_image)
        }catch (e: java.lang.Exception){}

        val cw = ContextWrapper(itemView.context)
        val directory = cw.getDir("MovieCovers", Context.MODE_PRIVATE)
        val myPath = File(directory, "${movie.id}.jpg") //saving image by id
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(myPath)
            image?.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun convertToValidString(movieTitle: String): String{ //function to create the correct title for searching the movie in the api
        val fragmentedString: ArrayList<String> = arrayListOf()
        var resultString = ""
        movieTitle.split(" ").toCollection(fragmentedString)
        for(i in 0 until fragmentedString.size - 1){
            resultString += fragmentedString[i] + "+"
        }
        resultString += fragmentedString[fragmentedString.size - 1]
        return resultString
    }

    private fun loadAPICovers(movieTitle: String): MovieOMDB? { //getting the post of the image
        val title: String = convertToValidString(movieTitle)
        val url = URL("http://www.omdbapi.com/?t=$title&apikey=$APIKEY")
        val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
        try {
            val input: InputStream = BufferedInputStream(urlConnection.inputStream)
            val response = readStream(input)
            return Gson().fromJson(response, MovieOMDB::class.java)
        }catch (e: Exception){
            //Do SOMETHING
        }finally {
            urlConnection.disconnect()
        }
        return null
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
}