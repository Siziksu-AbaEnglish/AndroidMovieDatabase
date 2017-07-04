package com.siziksu.tmdb.presenter;

import com.siziksu.tmdb.common.functions.Consumer;

public abstract class BasePresenter<V extends IBaseView> {

    protected V view;

    public void register(final V view) {
        this.view = view;
    }

    public void unregister() {
        this.view = null;
    }

    protected void doIfViewIsRegistered(Consumer consumer) {
        if (view != null) {
            consumer.consume();
        }
    }
}
