package com.zotikos.m4u.ui.popularmovies.list

import com.zotikos.m4u.ui.popularmovies.dto.MovieUIDto


sealed class MovieListAction {
    class MovieLoadingSuccessNew(val newMovies: List<MovieUIDto>) : MovieListAction()


}