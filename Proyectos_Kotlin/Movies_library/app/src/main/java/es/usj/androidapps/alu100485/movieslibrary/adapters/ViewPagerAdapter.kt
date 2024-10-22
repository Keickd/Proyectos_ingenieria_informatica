package es.usj.androidapps.alu100485.movieslibrary.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import es.usj.androidapps.alu100485.movieslibrary.fragments.FragmentMovies
import es.usj.androidapps.alu100485.movieslibrary.fragments.FragmentActors
import es.usj.androidapps.alu100485.movieslibrary.fragments.FragmentGenres

//Here i assign each fragment with the tabs
class ViewPagerAdapter(fm: FragmentManager, private val fM: FragmentMovies,
                       private val fA: FragmentActors, private val fG: FragmentGenres) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> { fM }
            1 -> { fA }
            2 -> { fG }
            else -> {  Fragment() }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position){
            0 -> {  return "Movies" }
            1 -> {  return "Actors" }
            2 -> {  return "Genres" }
        }
        return  super.getPageTitle(position)
    }
}
