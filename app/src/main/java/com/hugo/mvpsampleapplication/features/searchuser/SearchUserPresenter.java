package com.hugo.mvpsampleapplication.features.searchuser;

import com.hugo.mvpsampleapplication.features.UseCase;
import com.hugo.mvpsampleapplication.model.entities.User;
import com.hugo.mvpsampleapplication.features.DefaultSubscriber;
import com.hugo.mvpsampleapplication.model.network.SearchResponse;
import com.hugo.mvpsampleapplication.features.Presenter;

import com.hugo.mvpsampleapplication.utils.dependencyinjection.PerActivity;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * SearchUserPresenter controlling SearchUserFragment. SearchUserUseCase is used in order
 * to get data from the model. After data is fetched this presenter updates the SearchUserFragment
 * with the data.
 */
@PerActivity
public class SearchUserPresenter implements Presenter<SearchUserView> {

  private SearchUserView searchUserView;
  private UseCase searchUserUseCase;

  @Inject
  public SearchUserPresenter(@Named("searchUser") UseCase searchUserUseCase) {
    this.searchUserUseCase = searchUserUseCase;
  }

  @Override
  public void attachView(SearchUserView searchUserView) {
    this.searchUserView = searchUserView;
  }

  public void loadUsers(String username) {
    if(username == null || username.isEmpty()) {
      searchUserView.showMessage("Enter a username");
    } else {
      searchUserView.showProgressIndicator();
      searchUserUseCase.execute(new SearchUserSubscriber(), username);
    }
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
      searchUserView.showMessage("Error loading users");
    }

    @Override
    public void onNext(SearchResponse searchResponse) {
      List<User> users = searchResponse.getUsers();
      if (users.isEmpty()) {
        searchUserView.showMessage("No users found");
      }
      searchUserView.showUsers(users);
    }

  }

  @Override
  public void detachView() {
    this.searchUserView = null;
  }

  @Override
  public void destroy(boolean unsubscribe) {
    searchUserView = null;
    if(unsubscribe) {
      searchUserUseCase.unsubscribe();
    }
  }

}
