package com.example.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import com.example.movielist.data.MovieList
import com.example.movielist.networking.StandardApiCalls
import com.example.movielist.ui.MovieListViewController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
        lateinit var movieListViewController: MovieListViewController
        lateinit var viewContainer: ViewGroup
        lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewContainer = findViewById(R.id.main_view_container)
        progressBar = findViewById(R.id.progressBar)
        movieListViewController = MovieListViewController(this, layoutInflater, viewContainer)
        StandardApiCalls.loadNextPage(0, getString(R.string.api_key), object: Callback<MovieList> {

            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                if (response.isSuccessful) {
                    movieListViewController.updateData(response.body()!!.results, response.body()!!.page)
                    viewContainer.addView(movieListViewController.view)
                    progressBar.visibility = View.GONE
                } else {
                    onFailure(call, RuntimeException("Server responded with an error"))
                }
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                Log.e(this@MainActivity.localClassName, t.localizedMessage, t)
                Toast.makeText(this@MainActivity, R.string.list_error, Toast.LENGTH_LONG).show()
                showErrorState()
            }
        })
    }

    private fun showErrorState() {
        TODO("Not yet implemented")
    }
}