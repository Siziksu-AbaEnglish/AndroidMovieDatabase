package com.siziksu.tmdb;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;

import com.siziksu.tmdb.common.manager.PreferencesManager;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class PreferencesManagerTests {

    private PreferencesManager preferencesManager;

    @Before
    public void init() {
        Context context = InstrumentationRegistry.getTargetContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferencesManager = new PreferencesManager(sharedPreferences);
    }

    @Test
    public void settingAndReadingString() throws Exception {
        preferencesManager.removeKey("test");
        preferencesManager.setValue("test", "test_string");
        assertEquals("test_string", preferencesManager.getValue("test", "not_in_preferences"));
    }

    @Test
    public void notExistingKey() throws Exception {
        preferencesManager.removeKey("test");
        assertEquals("not_in_preferences", preferencesManager.getValue("test", "not_in_preferences"));
    }
}
