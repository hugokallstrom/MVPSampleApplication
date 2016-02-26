package com.hugo.mvpsampleapplication.features.searchuser;

import android.content.Context;

import com.hugo.mvpsampleapplication.model.entities.User;

import java.util.List;

/**
 * Created by hugo on 1/22/16.
 */
public interface SearchUserView {

    void showMessage(String message);

    void showProgressIndicator();

    void hideProgressIndicator();

    void showUsers(List<User> users);

    void startUserDetailsActivity(String username);

    Context getContext();
}
