package com.siziksu.tmdb.presenter;

import android.util.Log;

import com.siziksu.tmdb.R;
import com.siziksu.tmdb.common.functions.Consumer;

public abstract class BasePresenter<V extends IBaseView> {

    private static final String TAG = "BasePresenter";

    protected V view;

    public void register(final V view) {
        this.view = view;
    }

    public void unregister() {
        this.view = null;
    }

    protected void tellToTheViewIfItIsConnected(boolean isConnected, Consumer consumer) {
        doIfViewIsRegistered(
                () -> {
                    if (!isConnected) {
                        Log.e(TAG, view.getActivity().getResources().getString(R.string.connection_error));
                        consumer.consume();
                    }
                    view.showConnected(isConnected);
                });
    }

    protected void doIfViewIsRegistered(Consumer consumer) {
        if (view != null) {
            consumer.consume();
        }
    }
}
