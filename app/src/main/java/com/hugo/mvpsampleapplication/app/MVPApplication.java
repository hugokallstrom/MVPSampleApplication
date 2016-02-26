package com.hugo.mvpsampleapplication.app;

import android.app.Application;
import android.content.Context;

import com.hugo.mvpsampleapplication.model.network.GitHubService;

import com.hugo.mvpsampleapplication.utils.dependencyinjection.components.ApplicationComponent;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.components.DaggerApplicationComponent;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.modules.ApplicationModule;
import rx.Scheduler;
import rx.schedulers.Schedulers;

public class MVPApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if(applicationComponent == null) {
            applicationComponent =
                DaggerApplicationComponent.builder().applicationModule(new ApplicationModule()).build();
        }
    }

    public void setApplicationComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
