package es.usj.androidapps.alu100485.movieslibrary.adapters

import android.content.Intent
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.usj.androidapps.alu100485.movieslibrary.*
import es.usj.androidapps.alu100485.movieslibrary.asynctask.get.LoadMovieCover
import es.usj.androidapps.alu100485.movieslibrary.constants.*
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

//Here i display the movies in the perspective inside an actor
class AdapterMoviesViewActor: RecyclerView.Adapter<AdapterMoviesViewActor.ItemsAdapterVH>() {

    var list = ArrayList<Movie>()

    fun setData(list: ArrayList<Movie>) {
        this.list = arrayListOf()
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapterVH {
        return ItemsAdapterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemsAdapterVH, position: Int) {
        val movie = list[position]
        holder.render(movie)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemsAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnCreateContextMenuListener {
        fun render(movie: Movie) {

            if(checkDownloadedCover(itemView, movie))
                loadImageForAdapters(itemView, movie) //Loading images
            else
                LoadMovieCover(itemView, movie).execute()

            itemView.tvTitleItem.text = movie.title
            itemView.tvIdItem.text = movie.id.toString()
            itemView.tvDirectorItem.text = movie.director
            itemView.tvYearItem.text = movie.year.toString()
            itemView.tvRuntimeItem.text = movie.runtime.toString()
            itemView.tvRatingItem.text = movie.rating.toString()

            itemView.setOnClickListener {
                val intentToMovieFromActor = Intent(itemView.context, ViewMovie::class.java)
                intentToMovieFromActor.putExtra("movieInfo", movie)
                ContextCompat.startActivity(itemView.context, intentToMovieFromActor, null)
            }
            checkFavorites(itemView, movie)

            itemView.btnFavoriteItem.setOnClickListener { //managing favorites
                if(movie.favorite){
                    movie.favorite = false
                    itemView.btnFavoriteItem.setImageResource(R.drawable.ic_favorite_empty)
                }else{
                    movie.favorite = true
                    itemView.btnFavoriteItem.setImageResource(R.drawable.ic_favorite_filled)
                }
            }

            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, p2: ContextMenu.ContextMenuInfo?) {
            menu!!.add(this.adapterPosition, view?.id ?: 0, 0, EDIT)
            menu.add(this.adapterPosition, view?.id ?: 1, 1, DELETE)
        }
    }
}
