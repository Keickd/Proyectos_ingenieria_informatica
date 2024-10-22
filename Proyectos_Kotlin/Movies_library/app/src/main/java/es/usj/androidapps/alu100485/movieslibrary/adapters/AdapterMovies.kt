package es.usj.androidapps.alu100485.movieslibrary.adapters

import android.content.Intent
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import es.usj.androidapps.alu100485.movieslibrary.*
import es.usj.androidapps.alu100485.movieslibrary.asynctask.get.LoadMovieCover
import es.usj.androidapps.alu100485.movieslibrary.constants.*
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import es.usj.androidapps.alu100485.movieslibrary.singlenton.MoviesSingleton
import kotlinx.android.synthetic.main.item_movie.view.*

//Here i display the list of movies
class AdapterMovies: RecyclerView.Adapter<AdapterMovies.ItemsAdapterVH>(), Filterable {

    var list: ArrayList<Movie> = arrayListOf()
    var filteredList = ArrayList<Movie>()

    fun loadData(){
        list = arrayListOf()
        filteredList = arrayListOf()
        list = MoviesSingleton.movies as ArrayList<Movie>
        filteredList = MoviesSingleton.movies as ArrayList<Movie>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapterVH {
        return ItemsAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: ItemsAdapterVH, position: Int) {
        val movie = list[position]
        holder.render(movie)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ItemsAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {
        fun render(movie: Movie){
            loadDataFromAPI(itemView, movie)  //load the images

            itemView.tvTitleItem.text = movie.title
            itemView.tvIdItem.text = movie.id.toString()
            itemView.tvDirectorItem.text = movie.director
            itemView.tvYearItem.text = movie.year.toString()
            itemView.tvRuntimeItem.text = movie.runtime.toString()
            itemView.tvRatingItem.text = movie.rating.toString()

            itemView.setOnClickListener {
                val intentToMovie = Intent(itemView.context, ViewMovie::class.java)
                intentToMovie.putExtra("movieInfo", movie)
                startActivity(itemView.context, intentToMovie, null)
            }

            checkFavorites(itemView, movie)

            itemView.btnFavoriteItem.setOnClickListener {
                if(movie.favorite){
                    movie.favorite = false //here if won't be a favorite, i change the database and put on the icon
                    updateMovieIntoDatabase(itemView.context, movie)
                    itemView.btnFavoriteItem.setImageResource(R.drawable.ic_favorite_empty)
                }else{
                    movie.favorite = true
                    updateMovieIntoDatabase(itemView.context, movie)
                    itemView.btnFavoriteItem.setImageResource(R.drawable.ic_favorite_filled)
                }
            }
            itemView.itemCardView.setOnCreateContextMenuListener(this)
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, p2: ContextMenu.ContextMenuInfo?) {
            menu!!.add(this.adapterPosition, view?.id ?: 0, 0, EDIT)
            menu.add(this.adapterPosition, view?.id ?: 1, 1, DELETE) // options for the context menu
        }
    }

    private fun loadDataFromAPI(itemView: View, movie: Movie){
        LoadMovieCover(itemView, movie).execute()
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if(charSequence == null || charSequence.length < 0){
                    filterResults.count = filteredList.size
                    filterResults.values = filteredList
                }else{
                    var searchedSequence = charSequence.toString().toLowerCase()
                    val listFiltered = ArrayList<Movie>()

                    for(item in filteredList){
                        if(item.title.toLowerCase().contains(searchedSequence) || item.id.toString().contains(searchedSequence)) {
                            listFiltered.add(item)
                        }
                    }

                    filterResults.count = listFiltered.size
                    filterResults.values = listFiltered
                }
                return  filterResults
            }

            override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
                list = filterResults!!.values as ArrayList<Movie>
                notifyDataSetChanged()
            }
        }
    }


}