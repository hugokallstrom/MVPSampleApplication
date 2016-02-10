package com.hugo.mvpsampleapplication;

import android.app.Application;
import android.content.Context;

import com.hugo.mvpsampleapplication.model.network.GithubApi;

import rx.Scheduler;
import rx.schedulers.Schedulers;

public class MVPApplication extends Application {

    private GithubApi githubService;
    private Scheduler defaultSubscribeScheduler;

    public static MVPApplication get(Context context) {
        return (MVPApplication) context.getApplicationContext();
    }

    public GithubApi getGithubService() {
        if (githubService == null) {
            githubService = GithubApi.Factory.create();
        }
        return githubService;
    }

    //For setting mocks during testing
    public void setGithubService(GithubApi githubService) {
        this.githubService = githubService;
    }

    public Scheduler defaultSubscribeScheduler() {
        if (defaultSubscribeScheduler == null) {
            defaultSubscribeScheduler = Schedulers.io();
        }
        return defaultSubscribeScheduler;
    }

    //User to change scheduler from tests
    public void setDefaultSubscribeScheduler(Scheduler scheduler) {
        this.defaultSubscribeScheduler = scheduler;
    }
}
