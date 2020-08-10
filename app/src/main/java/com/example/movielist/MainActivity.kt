package com.example.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
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
    private lateinit var call: Call<MovieList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewContainer = findViewById(R.id.main_view_container)
        progressBar = findViewById(R.id.progressBar)
        movieListViewController = MovieListViewController(this, layoutInflater, viewContainer)
        call = StandardApiCalls.loadNextPage(this,0, getString(R.string.api_key), object: Callback<MovieList> {

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
                Log.e(MainActivity::class.java.simpleName, t.localizedMessage, t)
                Toast.makeText(this@MainActivity, R.string.list_error, Toast.LENGTH_LONG).show()
                showErrorState()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        if(!call.isExecuted|| !call.isCanceled) {
            call.cancel()
        }
        // Lets just simplify the lifecycle at bit for this project
        finish()
    }

    private fun showErrorState() {
        progressBar.visibility = View.GONE
        findViewById<TextView>(R.id.error).visibility = View.VISIBLE
    }
}