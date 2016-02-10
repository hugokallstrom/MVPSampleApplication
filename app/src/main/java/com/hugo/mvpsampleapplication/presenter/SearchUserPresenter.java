package com.hugo.mvpsampleapplication.presenter;

import com.hugo.mvpsampleapplication.MVPApplication;
import com.hugo.mvpsampleapplication.model.ErrorMessageFactory;
import com.hugo.mvpsampleapplication.model.network.DefaultSubscriber;
import com.hugo.mvpsampleapplication.model.network.GithubApi;
import com.hugo.mvpsampleapplication.model.network.SearchResponse;
import com.hugo.mvpsampleapplication.model.entities.User;
import com.hugo.mvpsampleapplication.view.fragment.SearchUserView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class SearchUserPresenter implements Presenter<SearchUserView>  {

    private SearchUserView searchUserView;
    private Subscription subscription;

    @Override
    public void attachView(SearchUserView searchUserView) {
        this.searchUserView = searchUserView;
    }

    @Override
    public void detachView() {}

    @Override
    public void destroy() {
        searchUserView = null;
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public void loadUsers(String username) {
        searchUserView.showProgressIndicator();
        MVPApplication application = getApplication();
        GithubApi githubService = getApi(application);
        subscription = githubService.searchUser(username)
                .delay(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new SearchUserSubscriber());
    }

    public MVPApplication getApplication() {
        return MVPApplication.get(searchUserView.getContext());
    }

    public GithubApi getApi(MVPApplication application) {
        return application.getGithubService();
    }

    public void onUserClick(String username) {
        searchUserView.startUserDetailsActivity(username);
    }

    private final class SearchUserSubscriber extends DefaultSubscriber<SearchResponse> {

        @Override
        public void onCompleted() {
            searchUserView.hideProgressIndicator();
        }

        @Override
        public void onError(Throwable e) {
            searchUserView.hideProgressIndicator();
            String errorMessage = ErrorMessageFactory.createErrorMessage((Exception) e);
            searchUserView.showMessage(errorMessage);
        }

        @Override
        public void onNext(SearchResponse searchResponse) {
            List<User> users = searchResponse.getUsers();
            if(users.isEmpty()) {
                searchUserView.showMessage("No results");
            }
            searchUserView.showUsers(users);
        }

    }
}
