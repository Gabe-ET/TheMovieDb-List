package com.example.movielist.data

data class MovieList (
    val page: Int,
    val results: List<Movie>
)

data class Movie (
    val id: Int,
    val popularity: Float,
    val vote_count: Int,
    val poster_path: String,
    val backdrop_path: String,
    val title: String,
    val vote_average: Float,
    val overview: String,
    val release_date: String,
    var page: Int = 0

)