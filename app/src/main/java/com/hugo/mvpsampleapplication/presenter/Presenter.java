package com.hugo.mvpsampleapplication.presenter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

    void destroy();
}
