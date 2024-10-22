package es.usj.androidapps.alu100485.movieslibrary.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.usj.androidapps.alu100485.movieslibrary.R
import es.usj.androidapps.alu100485.movieslibrary.ViewGenre
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Genre
import es.usj.androidapps.alu100485.movieslibrary.constants.genres
import kotlinx.android.synthetic.main.item_genre.view.*
//Here i display the list of genres
class AdapterGenres: RecyclerView.Adapter<AdapterGenres.ItemsAdapterVH>(), Filterable {

    var list = ArrayList<Genre>()
    var filteredList = ArrayList<Genre>()

    fun loadData() {
        list = arrayListOf()
        filteredList = arrayListOf()
        genres().toCollection(list) //load data
        genres().toCollection(filteredList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapterVH {
        return ItemsAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false))
    }

    override fun onBindViewHolder(holder: ItemsAdapterVH, position: Int) {
        val genre = list[position]
        holder.render(genre)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemsAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun render(genre: Genre){
            itemView.tvGenresNameItem.text = genre.name
            itemView.tvGenresIdItem.text = genre.id.toString()

            itemView.setOnClickListener {
                val intentToMovieFromGenres = Intent(itemView.context, ViewGenre::class.java)
                intentToMovieFromGenres.putExtra("genreInfo", genre)
                ContextCompat.startActivity(itemView.context, intentToMovieFromGenres, null)
            }
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
                    val searchedSequence = charSequence.toString().toLowerCase()
                    val listFiltered = ArrayList<Genre>()

                    for(item in filteredList){
                        if(item.name.toLowerCase().contains(searchedSequence) || item.id.toString().contains(searchedSequence))
                            listFiltered.add(item)
                    }

                    filterResults.count = listFiltered.size
                    filterResults.values = listFiltered
                }
                return  filterResults
            }

            override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
                list = filterResults!!.values as ArrayList<Genre>
                notifyDataSetChanged()
            }
        }
    }
}