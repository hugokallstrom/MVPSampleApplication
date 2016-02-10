package com.hugo.mvpsampleapplication.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hugo on 1/22/16.
 */
public class Repository {

    private int id;
    private String name;
    private String description;
    private String language;
    private int forks;
    private int watchers;
    @SerializedName("stargazers_count")
    private int stars;

    public Repository() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getWatchers() {
        return watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
