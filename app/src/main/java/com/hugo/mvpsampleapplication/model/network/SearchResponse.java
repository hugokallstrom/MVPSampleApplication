package com.hugo.mvpsampleapplication.model.network;

import com.google.gson.annotations.SerializedName;
import com.hugo.mvpsampleapplication.model.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 1/26/16.
 */
public class SearchResponse {
    @SerializedName("total_count")
    private int totalCount;
    @SerializedName("incomplete_results")
    private boolean incompleteResults;
    @SerializedName("items")
    private List<User> users = new ArrayList<User>();

    public SearchResponse() {}

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
