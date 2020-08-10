package com.example.movielist.data

data class MovieDetail (
    val genres: List<Genre>,
    val id: Int,
    val title: String,
    val overview: String,
    val vote_average: Float,
    val poster_path: String,
    val backdrop_path: String
)

data class Genre (
    val code: Int,
    val name: String
)