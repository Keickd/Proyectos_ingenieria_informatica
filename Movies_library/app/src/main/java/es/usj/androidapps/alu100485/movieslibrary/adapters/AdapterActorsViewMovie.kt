package es.usj.androidapps.alu100485.movieslibrary.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.usj.androidapps.alu100485.movieslibrary.R
import es.usj.androidapps.alu100485.movieslibrary.ViewActor
import es.usj.androidapps.alu100485.movieslibrary.dataclass.Actor
import kotlinx.android.synthetic.main.item_actors_genres_view_movie.view.*

//Here is when we are seeing a movie and we display the list of actors
class AdapterActorsViewMovie: RecyclerView.Adapter<AdapterActorsViewMovie.ItemsAdapterVH>() {

    var list = ArrayList<Actor>()

    fun setData(list: ArrayList<Actor>) {
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsAdapterVH {
        return ItemsAdapterVH(
            LayoutInflater.from(parent.context).inflate(R.layout.item_actors_genres_view_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemsAdapterVH, position: Int) {
        val actor = list[position]
        holder.render(actor)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ItemsAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun render(actor: Actor) {
            itemView.tvItemActorGenreViewMovie.text = actor.name

            itemView.setOnClickListener {
                //Toast.makeText(itemView.context, "You selected ${actor.name}", Toast.LENGTH_SHORT) .show()
                val intentToActorFromMovie = Intent(itemView.context, ViewActor::class.java)
                intentToActorFromMovie.putExtra("actorInfo", actor)
                ContextCompat.startActivity(itemView.context, intentToActorFromMovie, null)
            }
        }
    }
}



