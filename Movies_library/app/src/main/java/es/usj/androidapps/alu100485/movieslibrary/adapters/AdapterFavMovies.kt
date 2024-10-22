package es.usj.androidapps.alu100485.movieslibrary.adapters

import android.content.Intent
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.usj.androidapps.alu100485.movieslibrary.*
import es.usj.androidapps.alu100485.movieslibrary.asynctask.get.LoadMovieCover
import es.usj.androidapps.alu100485.movieslibrary.constants.*
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

//Adapter for the list of favorite movies
class AdapterFavMovies: RecyclerView.Adapter<AdapterFavMovies.ItemsAdapterVH>(), Filterable {

    var filteredList = ArrayList<Movie>()
    var favList = ArrayList<Movie>()


    fun loadData(){
        this.favList = arrayListOf()
        favs().toCollection(favList)
        this.filteredList = favList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapterVH {
        return ItemsAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false))
    }

    override fun onBindViewHolder(holder: ItemsAdapterVH, position: Int) {
        val favMovie = favs()[position]
        holder.render(favMovie)
    }

    override fun getItemCount(): Int {
        return favs().size
    }

    inner class ItemsAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener{
        fun render(favMovie: Movie){

            if(checkDownloadedCover(itemView, favMovie))
                loadImageForAdapters(itemView, favMovie) // here i check if the image is downloaded for each item
            else
                LoadMovieCover(itemView, favMovie).execute()

            itemView.tvTitleItem.text = favMovie.title
            itemView.tvIdItem.text = favMovie.id.toString()
            itemView.tvDirectorItem.text = favMovie.director
            itemView.tvYearItem.text = favMovie.year.toString()
            itemView.tvRuntimeItem.text = favMovie.runtime.toString()
            itemView.tvRatingItem.text = favMovie.rating.toString()

            itemView.setOnClickListener {
                val intentToMovie = Intent(itemView.context, ViewMovie::class.java)
                intentToMovie.putExtra("movieInfo", favMovie)
                ContextCompat.startActivity(itemView.context, intentToMovie, null)
            }

            checkFavorites(favMovie)

            itemView.btnFavoriteItem.setOnClickListener {
                if(favMovie.favorite){ //if i click the heart the movie won't be a favorite, i also update the database
                    favMovie.favorite = false
                    updateMovieIntoDatabase(itemView.context, favMovie)
                    notifyItemChanged(adapterPosition)
                    loadData()
                }
            }
            itemView.itemCardView.setOnCreateContextMenuListener(this)
            itemView.setOnCreateContextMenuListener(this)
        }
        private fun checkFavorites(favMovie: Movie){
            if(favMovie.favorite)
                itemView.btnFavoriteItem.setImageResource(R.drawable.ic_favorite_filled)
        }

        override fun onCreateContextMenu(menu: ContextMenu?, view: View?, p2: ContextMenu.ContextMenuInfo?) {
            menu!!.add(this.adapterPosition, view?.id ?: 0, 0, EDIT)
            menu.add(this.adapterPosition, view?.id ?: 1, 1, DELETE)
        }
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
                        if(item.title.toLowerCase().contains(searchedSequence) || item.id.toString().contains(searchedSequence))
                            listFiltered.add(item)
                    }

                    filterResults.count = listFiltered.size
                    filterResults.values = listFiltered
                }
                return  filterResults
            }

            override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
                favList = filterResults!!.values as ArrayList<Movie>
                notifyDataSetChanged()
            }
        }
    }
}