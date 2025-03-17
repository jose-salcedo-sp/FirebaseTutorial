package com.example.firebasetutorial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MoviesViewModel : ViewModel() {
    val moviesList = MutableLiveData<ArrayList<Movie>>()

    fun updateMoviesList(newList: ArrayList<Movie>) {
        moviesList.value = newList
    }
}