package com.siziksu.tmdb.presenter;

import android.app.Activity;

public interface IBaseView {

    Activity getActivity();

    void showProgress(boolean value);

    void showConnected(boolean value);

    void connectionTimeout();
}
