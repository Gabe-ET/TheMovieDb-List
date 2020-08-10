package com.example.movielist.networking

import com.example.movielist.data.MovieDetail
import com.example.movielist.data.MovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface TmdbApi {

    @GET("/3/discover/movie")
    fun discoverMovies(@QueryMap map: Map<String, String>): Call<MovieList>

    @GET("/3/movie/{movie_id}")
    fun getMovie(@Path("movie_id") movieId: Int, @QueryMap map: Map<String, String>): Call<MovieDetail>
}