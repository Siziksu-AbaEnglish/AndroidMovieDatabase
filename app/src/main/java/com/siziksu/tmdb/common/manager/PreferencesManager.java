package com.siziksu.tmdb.common.manager;

import android.content.SharedPreferences;

import com.siziksu.tmdb.common.Constants;

import javax.inject.Inject;

public final class PreferencesManager {

    @Inject
    SharedPreferences sharedPreferences;

    public PreferencesManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setPoserUrl(String url) {
        setValue(Constants.PREFERENCES_POSTER_URL_KEY, url);
    }

    public void setBackdropUrl(String url) {
        setValue(Constants.PREFERENCES_BACKDROP_URL_KEY, url);
    }

    public void tutorialCompleted(Boolean value) {
        setValue(Constants.PREFERENCES_SHOW_TUTORIAL_KEY, value);
    }

    public boolean isTutorialCompleted() {
        return getValue(Constants.PREFERENCES_SHOW_TUTORIAL_KEY, false);
    }

    public void removeKey(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public void setValue(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public void setValue(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public String getValue(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public boolean getValue(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }
}