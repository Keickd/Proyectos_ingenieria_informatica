package es.usj.androidapps.alu100485.movieslibrary.singlenton

import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import kotlin.collections.HashSet

object FavoriteMoviesSingleton {
    var favoriteMovies: ArrayList<Movie> = arrayListOf()
}