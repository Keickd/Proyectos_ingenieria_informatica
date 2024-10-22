package es.usj.androidapps.alu100485.movieslibrary

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import es.usj.androidapps.alu100485.movieslibrary.adapters.ViewPagerAdapter
import es.usj.androidapps.alu100485.movieslibrary.constants.hideKeyboard
import es.usj.androidapps.alu100485.movieslibrary.fragments.MyFragment
import kotlinx.android.synthetic.main.activity_home.*


class Home : AppCompatActivity() {

    private var myFragmentMovies: MyFragment = MyFragment()
    private var myFragmentActors: MyFragment = MyFragment()
    private var myFragmentGenres: MyFragment = MyFragment()

    lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tabsSetUp()
        favoritesButton()
        addMovieButton()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_nav, menu)
        val menuItem = menu!!.findItem(R.id.svSearch)
        searchView = menuItem.actionView as SearchView //Creating the menu on the top right corner
        searchView.maxWidth = Int.MAX_VALUE
        filtering(0)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.optionsForContact -> {
                val intentFromMovieListToContact = Intent(this, Contact::class.java)
                startActivity(intentFromMovieListToContact)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun favoritesButton(){
        btnFavorites.setOnClickListener {
            val intentFromMovieListToFavorites = Intent(this, Favorites::class.java)
            startActivity(intentFromMovieListToFavorites)
        }
    }

    private fun addMovieButton(){
        btnAddMovie.setOnClickListener {
            val intentFromMovieListToAddMovie = Intent(this, AddMovie::class.java)
            startActivity(intentFromMovieListToAddMovie)
        }
    }

    private fun tabsSetUp(){
        viewPager.adapter = ViewPagerAdapter( //setting the tabs and the viewpager
            supportFragmentManager,
            myFragmentMovies.fragmentMovies,
            myFragmentActors.fragmentActors,
            myFragmentGenres.fragmentGenres
        )
        tlGeneral.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                searchView.setQuery("", false)
                hideKeyboard(this@Home)
                filtering(position)
            }
        })
        settingTabIcons()
    }

    private fun settingTabIcons(){
        tlGeneral.getTabAt(0)!!.setIcon(R.drawable.ic_movie)
        tlGeneral.getTabAt(1)!!.setIcon(R.drawable.ic_actor)
        tlGeneral.getTabAt(2)!!.setIcon(R.drawable.ic_genre)
    }

    private fun filtering(position: Int){
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(filterString: String?): Boolean {
                filteringByTabs(position, filterString)
                hideKeyboard(this@Home)
                return true
            }

            override fun onQueryTextChange(filterString: String?): Boolean {
                filteringByTabs(position, filterString)
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard(this@Home)
    }

    private fun filteringByTabs(position: Int, filterString: String?){
        when(position){
            0 -> {
                myFragmentMovies.fragmentMovies.adapterMovies?.filter?.filter(filterString)
            }
            1 -> {
                myFragmentActors.fragmentActors.adapterActors?.filter?.filter(filterString)
            }
            2 -> {
                myFragmentGenres.fragmentGenres.adapterGenres?.filter?.filter(filterString)
            }
        }
    }


}