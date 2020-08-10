package com.example.movielist.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movielist.DetailActivity
import com.example.movielist.MOVIE_ID
import com.example.movielist.R
import com.example.movielist.data.Movie
import com.example.movielist.data.MovieList
import com.example.movielist.networking.StandardApiCalls
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListViewController (private val context: Context, inflater: LayoutInflater, container: ViewGroup){
    var pages = 0
    val view: RecyclerView = inflater.inflate(R.layout.movie_list_container, container, false) as RecyclerView
    private val adapter = object: RecyclerView.Adapter<MovieViewHolder>(){
        val movieList: ArrayList<Movie> = arrayListOf()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item, parent, false)
            return MovieViewHolder(view)
        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
            if (pages - movieList[position].page < 1){
                loadMoreMovies(pages)
                pages++
            }
            return holder.bind(movieList[position])
        }

        fun addMovies(movies: List<Movie>) {
            movieList.addAll(movies)
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return movieList.size
        }

    }

    class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val posterView: ImageView = itemView.findViewById<ImageView>(R.id.poster)
        private val titleView: TextView = itemView.findViewById<TextView>(R.id.title)
        private val extraView: TextView = itemView.findViewById<TextView>(R.id.extra)
        fun bind(movie: Movie) {
            Picasso.get()
                .load(itemView.context.getString(R.string.list_poster_path_prefix) + movie.poster_path)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .into(posterView)
            titleView.text = movie.title
            extraView.text = movie.vote_average.toString()
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(MOVIE_ID, movie.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    init {
        view.layoutManager = LinearLayoutManager(context)
        view.adapter = adapter
    }

    fun updateData(movies: List<Movie>, page: Int) {
        for (movie: Movie in movies) {
            movie.page = page
            //try and fetch all images even if they aren't yet shown on the ui
            Picasso.get().load(context.getString(R.string.list_poster_path_prefix) + movie.poster_path).fetch()
        }
        adapter.addMovies(movies)
        pages = page
    }

    fun loadMoreMovies( page: Int){
        StandardApiCalls.loadNextPage(page, context.getString(R.string.api_key), object: Callback<MovieList> {

            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.isSuccessful) {
                    updateData(response.body()!!.results, response.body()!!.page)
                } else {
                    onFailure(call, RuntimeException("Server responded with an error"))
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

}