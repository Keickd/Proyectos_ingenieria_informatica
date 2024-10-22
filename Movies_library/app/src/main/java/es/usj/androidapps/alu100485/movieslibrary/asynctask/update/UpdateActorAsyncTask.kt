package es.usj.androidapps.alu100485.movieslibrary.asynctask.update

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import es.usj.androidapps.alu100485.movieslibrary.constants.*
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Actor
import es.usj.androidapps.alu100485.movieslibrary.dataclass.ApiResponse
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
//Update method the same for genre actor movie
class UpdateActorAsyncTask: AsyncTask<Actor, Void, Void>() {
    override fun doInBackground(vararg params: Actor?): Void? {
        var result : String = ""
        val url = URL("http://$SERVER:8888/user/$UPDATEACTOR.php?user=$USER&pass=$PASSWORD")
        val connection = url.openConnection() as HttpURLConnection
        var responseCode = 0
        connection.requestMethod = "POST"
        connection.readTimeout = 1500
        connection.connectTimeout = 1500
        connection.doOutput = true
        connection.setRequestProperty("Content-Type", "application/json;utf-8")
        connection.setRequestProperty("Accept", "*/*")
        try {
            if (!params.isNullOrEmpty()) {
                val writer = DataOutputStream(connection.outputStream)
                val json = Gson().toJson(params[0]!!)
                writer.write(json.toByteArray(StandardCharsets.UTF_8))
                writer.flush()
                writer.close()
                responseCode = connection.responseCode
                if (responseCode == 200) {
                    result = readStream(connection.inputStream)
                    val response = Gson().fromJson(result, ApiResponse::class.java)
                    Log.e("result: $response", "")
                }
            }
        }catch (e: Exception){}

        return null
    }

    private fun readStream(inputStream : InputStream) : String {
        val br = BufferedReader(InputStreamReader(inputStream))
        val total = StringBuilder()
        while (true) {
            val line = br.readLine() ?: break
            total.append(line)
        }
        val totalString = total.toString()
        return totalString
    }
}
