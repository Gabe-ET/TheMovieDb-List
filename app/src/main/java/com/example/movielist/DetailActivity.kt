package com.example.movielist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.movielist.data.MovieDetail
import com.example.movielist.networking.StandardApiCalls
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val MOVIE_ID = "DetailActivity.MOVIE_ID"

class DetailActivity : AppCompatActivity() {
    lateinit var viewContainer: ViewGroup
    lateinit var progressBar: ProgressBar
    private lateinit var call: Call<MovieDetail>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewContainer = findViewById(R.id.main_view_container)
        progressBar = findViewById(R.id.progressBar)
        val id: Int? = intent.extras?.get(MOVIE_ID) as Int
        id?.let {
            call = StandardApiCalls.loadMovie(this, id, getString(R.string.api_key), object : Callback<MovieDetail> {
                override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                    if (response.isSuccessful) {
                        progressBar.visibility = View.GONE
                        setContentView(R.layout.detail_view)
                        response.body()?.let { populateDetailView(it) }
                            ?: onFailure(call, RuntimeException("Server responded empty body"))
                    } else {
                        onFailure(call, RuntimeException("Server responded with an error"))
                    }
                }

                override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                    closeOnError(t)
                }
            })
        }?:closeOnError(RuntimeException("No movie id was passed to detail activity"))
    }

    private fun closeOnError(t: Throwable) {
        Log.e(DetailActivity::class.java.simpleName, t.localizedMessage, t)
        Toast.makeText(this@DetailActivity, R.string.detail_error, Toast.LENGTH_LONG).show()
        this@DetailActivity.finish()
    }

    override fun onStop() {
        super.onStop()
        if(!call.isCanceled || !call.isCanceled) {
            call.cancel()
        }
    }

    fun populateDetailView(movieDetail: MovieDetail){
        val poster: ImageView = findViewById(R.id.poster)
        val titleView: TextView = findViewById(R.id.title)
        val overviewView: TextView = findViewById(R.id.overview)
        val ratingView: TextView = findViewById(R.id.rating)
        val genresView: TextView = findViewById(R.id.genres)

        var genreString = ""
        for (genre in movieDetail.genres) {
            genreString = genreString + genre.name + ", "
        }
        genreString = genreString.removeSuffix(", ")

        Picasso.get()
            .load(getString(R.string.detail_poster_path_prefix) + movieDetail.poster_path)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_close_clear_cancel)
            .into(poster)
        titleView.text = movieDetail.title
        overviewView.text = movieDetail.overview
        ratingView.text = movieDetail.vote_average.toString()
        genresView.text = genreString
    }

}