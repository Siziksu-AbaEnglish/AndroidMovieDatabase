package com.siziksu.tmdb.common.model.response.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Images {

    @SerializedName("base_url")
    @Expose
    public String baseUrl;
    @SerializedName("backdrop_sizes")
    @Expose
    public List<String> backdropSizes = null;
    @SerializedName("poster_sizes")
    @Expose
    public List<String> posterSizes = null;
}
