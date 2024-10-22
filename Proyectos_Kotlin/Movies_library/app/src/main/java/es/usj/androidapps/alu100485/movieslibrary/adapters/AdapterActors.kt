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
import es.usj.androidapps.alu100485.movieslibrary.ViewActor
import es.usj.androidapps.alu100485.movieslibrary.constants.actors
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Actor
import kotlinx.android.synthetic.main.item_actor.view.*

//Here i display the list of actors
class AdapterActors: RecyclerView.Adapter<AdapterActors.ItemsAdapterVH>(), Filterable {

    var list = ArrayList<Actor>()
    var filteredList = ArrayList<Actor>()

    fun loadData(){
        list = arrayListOf()
        filteredList = arrayListOf()
        actors().toCollection(list) //Load data from the singleton
        actors().toCollection(filteredList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapterVH {
        return ItemsAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.item_actor, parent, false))
    }

    override fun onBindViewHolder(holder: ItemsAdapterVH, position: Int) {
        val actor = list[position]
        holder.render(actor)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemsAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun render(actor: Actor){
            itemView.tvActorsNameItem.text = actor.name
            itemView.tvActorsIdItem.text = actor.id.toString()

            itemView.setOnClickListener {
                val intentToActor = Intent(itemView.context, ViewActor::class.java)
                intentToActor.putExtra("actorInfo", actor)  //when i click i want to go to the actors view activity
                ContextCompat.startActivity(itemView.context, intentToActor, null)
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
                    var searchedSequence = charSequence.toString().toLowerCase()
                    val listFiltered = ArrayList<Actor>()

                    for(item in filteredList){
                        if(item.name.toLowerCase().contains(searchedSequence) || item.id.toString().contains(searchedSequence)) //Filtering by title and id
                            listFiltered.add(item)
                    }

                    filterResults.count = listFiltered.size
                    filterResults.values = listFiltered
                }
                return  filterResults
            }

            override fun publishResults(p0: CharSequence?, filterResults: FilterResults?) {
                list = filterResults!!.values as ArrayList<Actor>
                notifyDataSetChanged()
            }
        }
    }
}