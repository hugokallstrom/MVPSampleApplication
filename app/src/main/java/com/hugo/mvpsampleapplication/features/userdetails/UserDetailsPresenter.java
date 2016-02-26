package com.hugo.mvpsampleapplication.features.userdetails;

import com.hugo.mvpsampleapplication.features.Presenter;
import com.hugo.mvpsampleapplication.features.UseCase;
import com.hugo.mvpsampleapplication.features.DefaultSubscriber;
import com.hugo.mvpsampleapplication.model.entities.Repository;

import com.hugo.mvpsampleapplication.utils.dependencyinjection.PerActivity;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * UserDetailsPresenter controlling UserDetailsFragment. LoadUserDetailsUseCase is used in order
 * to get data from the model. After data is fetched this presenter updates the UserDetailsFragment
 * with the data.
 */
@PerActivity
public class UserDetailsPresenter implements Presenter<UserDetailsView> {

  private UserDetailsView userDetailsView;
  private UseCase loadUserDetailsUseCase;

  @Inject
  public UserDetailsPresenter(@Named("userDetails") UseCase loadUserDetailsUseCase) {
    this.loadUserDetailsUseCase = loadUserDetailsUseCase;
  }

  @Override
  public void attachView(UserDetailsView userDetailsView) {
    this.userDetailsView = userDetailsView;
  }

  public void loadRepositories(String username) {
    userDetailsView.showProgressIndicator();
    loadUserDetailsUseCase.execute(new LoadRepositoriesSubscriber(), username);
  }

  private final class LoadRepositoriesSubscriber extends DefaultSubscriber<List<Repository>> {

    @Override
    public void onCompleted() {
      userDetailsView.hideProgressIndicator();
    }
    @Override
    public void onError(Throwable e) {
      userDetailsView.hideProgressIndicator();
      userDetailsView.showMessage("Error loading repositories");
      e.printStackTrace();
    }

    @Override
    public void onNext(List<Repository> repositories) {
      if (repositories.isEmpty()) {
        userDetailsView.showMessage("No public repositories");
      }
      userDetailsView.showRepositories(repositories);
    }

  }

  @Override
  public void detachView() {
    this.userDetailsView = null;
  }

  @Override
  public void destroy(boolean unsubscribe) {
    userDetailsView = null;
    if(unsubscribe) {
      loadUserDetailsUseCase.unsubscribe();
    }
  }

}
