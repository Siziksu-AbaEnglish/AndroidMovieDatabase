package com.siziksu.tmdb.common.model.response.configuration;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Configuration {

    @SerializedName("images")
    @Expose
    public Images images;
}
