package com.example.movielist.networking

import com.example.movielist.data.MovieDetail
import com.example.movielist.data.MovieList
import retrofit2.Call
import retrofit2.Callback

object StandardApiCalls {

    fun loadNextPage(currentPage: Int, key: String, callback: Callback<MovieList>): Call<MovieList> {
        val nextPage = currentPage + 1
        val request = RequestBuilder.buildRequest(TmdbApi::class.java)
        val map = mapOf(
            "api_key" to key,
            "language" to "en-US",
            "sort_by" to "popularity.desc",
            "include_adult" to "include_adult",
            "include_video" to "false",
            "page" to nextPage.toString()
        )
        val call = request.discoverMovies(map)

        call.enqueue(callback)
        // retuning the call allows the UI layer to cancel it, should the user exit the app
        return call

    }

    fun loadMovie(movieId: Int, key: String, callback: Callback<MovieDetail>): Call<MovieDetail> {
        val request = RequestBuilder.buildRequest(TmdbApi::class.java)
        val map = mapOf("api_key" to key, "language" to "en-US")
        val call = request.getMovie(movieId, map)

        call.enqueue(callback)
        // retuning the call allows the UI layer to cancel it, should the user exit the app
        return call

    }
}