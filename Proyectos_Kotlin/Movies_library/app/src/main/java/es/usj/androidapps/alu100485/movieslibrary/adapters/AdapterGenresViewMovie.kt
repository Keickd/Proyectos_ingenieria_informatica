package es.usj.androidapps.alu100485.movieslibrary.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.usj.androidapps.alu100485.movieslibrary.R
import es.usj.androidapps.alu100485.movieslibrary.ViewGenre
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Genre
import kotlinx.android.synthetic.main.item_actors_genres_view_movie.view.*

//this adapter is to display a list of genres in the view movie activity
class AdapterGenresViewMovie: RecyclerView.Adapter<AdapterGenresViewMovie.ItemsAdapterVH>() {

    var list = ArrayList<Genre>()

    fun setData(list: ArrayList<Genre>) {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapterVH {
        return ItemsAdapterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_actors_genres_view_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemsAdapterVH, position: Int) {
        val genre = list[position]
        holder.render(genre)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemsAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun render(genre: Genre) {
            itemView.tvItemActorGenreViewMovie.text = genre.name

            itemView.setOnClickListener {
                val intentToGenreFromMovie = Intent(itemView.context, ViewGenre::class.java)
                intentToGenreFromMovie.putExtra("genreInfo", genre)
                ContextCompat.startActivity(itemView.context, intentToGenreFromMovie, null)
            }
        }
    }
}