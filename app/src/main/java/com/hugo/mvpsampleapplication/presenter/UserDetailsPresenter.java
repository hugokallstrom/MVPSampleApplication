package com.hugo.mvpsampleapplication.presenter;

import com.hugo.mvpsampleapplication.MVPApplication;
import com.hugo.mvpsampleapplication.model.ErrorMessageFactory;
import com.hugo.mvpsampleapplication.model.network.DefaultSubscriber;
import com.hugo.mvpsampleapplication.model.network.GithubApi;
import com.hugo.mvpsampleapplication.model.entities.Repository;
import com.hugo.mvpsampleapplication.view.fragment.UserDetailsView;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by hugo on 1/26/16.
 */
public class UserDetailsPresenter implements Presenter<UserDetailsView> {

    private UserDetailsView userDetailsView;
    private Subscription subscription;

    @Override
    public void attachView(UserDetailsView userDetailsView) {
        this.userDetailsView = userDetailsView;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void destroy() {
        userDetailsView = null;
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public void loadReposetories(String username) {
        userDetailsView.showProgressIndicator();
        MVPApplication application = MVPApplication.get(userDetailsView.getContext());
        GithubApi githubService = application.getGithubService();
        subscription = githubService.getReposetoriesFromUser(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new LoadReposetoriesSubscriber());
    }

    private final class LoadReposetoriesSubscriber extends DefaultSubscriber<List<Repository>> {
        @Override
        public void onCompleted() {
            userDetailsView.hideProgressIndicator();
        }

        @Override
        public void onError(Throwable e) {
            userDetailsView.hideProgressIndicator();
            String errorMessage = ErrorMessageFactory.createErrorMessage((Exception) e);
            userDetailsView.showMessage(errorMessage);
            e.printStackTrace();
        }

        @Override
        public void onNext(List<Repository> repositories) {
            userDetailsView.showRepositories(repositories);
        }
    }
}
