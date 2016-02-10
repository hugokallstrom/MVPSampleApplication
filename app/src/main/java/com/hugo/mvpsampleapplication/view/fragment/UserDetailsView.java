package com.hugo.mvpsampleapplication.view.fragment;

import android.content.Context;

import com.hugo.mvpsampleapplication.model.entities.Repository;

import java.util.List;

/**
 * Created by hugo on 1/26/16.
 */
public interface UserDetailsView {

    void showMessage(String message);

    void showProgressIndicator();

    void hideProgressIndicator();

    void showRepositories(List<Repository> repositories);

    Context getContext();

}
