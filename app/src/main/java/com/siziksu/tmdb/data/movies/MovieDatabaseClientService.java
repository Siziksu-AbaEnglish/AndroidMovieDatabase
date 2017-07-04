package com.siziksu.tmdb.data.movies;

import com.siziksu.tmdb.common.model.response.configuration.Configuration;
import com.siziksu.tmdb.common.model.response.movies.Movies;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDatabaseClientService {

    String URI = "/3";
    String URI_GET_CONFIGURATION = URI + "/configuration";
    String URI_TOP_RATED_MOVIES = URI + "/movie/top_rated";
    String URI_SEARCH_MOVIE_BY_NAME = URI + "/search/movie?sort_by=popularity.desc";
    String URI_SIMILAR_MOVIES = URI + "/movie/{movie_id}/similar";

    @GET(URI_GET_CONFIGURATION)
    Observable<Configuration> getConfiguration(
            @Query("api_key") String apiKey
    );

    @GET(URI_TOP_RATED_MOVIES)
    Observable<Movies> getTopRatedMovies(
            @Query("page") Integer page,
            @Query("include_adult") Boolean includeAdult,
            @Query("api_key") String apiKey
    );

    @GET(URI_SEARCH_MOVIE_BY_NAME)
    Observable<Movies> searchMovieByName(
            @Query("page") Integer page,
            @Query("query") String query,
            @Query("include_adult") Boolean includeAdult,
            @Query("api_key") String apiKey
    );

    @GET(URI_SIMILAR_MOVIES)
    Observable<Movies> getSimilarMovies(
            @Path("movie_id") Integer movieId,
            @Query("page") Integer page,
            @Query("include_adult") Boolean includeAdult,
            @Query("api_key") String apiKey
    );
}
