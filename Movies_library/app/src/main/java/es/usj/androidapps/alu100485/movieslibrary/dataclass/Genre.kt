package es.usj.androidapps.alu100485.movieslibrary.dataclass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(val id: Int, var name: String): Parcelable