package com.siziksu.tmdb.common.utils;

import android.text.TextUtils;
import android.util.Log;

import com.siziksu.tmdb.common.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DatesUtilities {

    private static final String TAG = "DatesManager";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String YEAR_FORMAT = "yyyy";

    public static String getYear(String year) {
        if (TextUtils.isEmpty(year)) {
            return Constants.NA;
        }
        String yearFormatted = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            Date date = format.parse(year);
            yearFormatted = new SimpleDateFormat(YEAR_FORMAT, Locale.getDefault()).format(date);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return !TextUtils.isEmpty(yearFormatted) ? yearFormatted : year;
    }
}
