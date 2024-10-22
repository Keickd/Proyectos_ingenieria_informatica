package es.usj.androidapps.alu100485.movieslibrary.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    var id: Int,
    var title: String,
    var genres: ArrayList<Int>?,
    var description: String,
    var director: String,
    var actors: ArrayList<Int>?,
    var year: Int,
    var runtime: Int,
    var rating: Float,
    var votes: Int,
    var revenue: Float,
    var favorite: Boolean): Parcelable{
}
