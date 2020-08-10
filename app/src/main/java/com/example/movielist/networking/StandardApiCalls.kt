package com.example.movielist.networking

import android.content.Context
import com.example.movielist.R
import com.example.movielist.data.MovieDetail
import com.example.movielist.data.MovieList
import retrofit2.Call
import retrofit2.Callback

object StandardApiCalls {

    fun loadNextPage(context: Context, currentPage: Int, key: String, callback: Callback<MovieList>): Call<MovieList> {
        val nextPage = currentPage + 1
        val request = RequestBuilder.buildRequest(TmdbApi::class.java)
        val map = mapOf(
            context.getString(R.string.api_key_param) to key,
            context.getString(R.string.language_param) to context.getString(R.string.language_value),
            context.getString(R.string.sort_param) to context.getString(R.string.sort_value),
            context.getString(R.string.adult_param) to context.getString(R.string.adult_value),
            context.getString(R.string.video_param) to context.getString(R.string.video_value),
            context.getString(R.string.page_param) to nextPage.toString()
        )
        val call = request.discoverMovies(map)

        call.enqueue(callback)
        // retuning the call allows the UI layer to cancel it, should the user exit the app
        return call

    }

    fun loadMovie(context: Context, movieId: Int, key: String, callback: Callback<MovieDetail>): Call<MovieDetail> {
        val request = RequestBuilder.buildRequest(TmdbApi::class.java)
        val map = mapOf(context.getString(R.string.api_key_param)  to key,
            context.getString(R.string.language_param) to context.getString(R.string.language_value))
        val call = request.getMovie(movieId, map)

        call.enqueue(callback)
        // retuning the call allows the UI layer to cancel it, should the user exit the app
        return call

    }
}