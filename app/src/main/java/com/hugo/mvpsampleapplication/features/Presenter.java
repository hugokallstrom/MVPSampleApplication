package com.hugo.mvpsampleapplication.features;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

    void destroy(boolean unsubscribe);
}
