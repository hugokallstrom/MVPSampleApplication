package com.hugo.mvpsampleapplication.features.userdetails;

import com.hugo.mvpsampleapplication.MockFactory;
import com.hugo.mvpsampleapplication.model.entities.Repository;
import com.hugo.mvpsampleapplication.model.network.GitHubService;
import com.hugo.mvpsampleapplication.utils.PostExecutionThread;
import com.hugo.mvpsampleapplication.utils.ThreadExecutor;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import retrofit.HttpException;
import retrofit.Response;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by hugo on 2/25/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserDetailsPresenterTest {

  @Mock private GitHubService gitHubService;
  @Mock private ThreadExecutor threadExecutor;
  @Mock private PostExecutionThread postExecutionThread;
  @Mock private LoadUserDetailsUseCase mockLoadUserDetailsUseCase;

  @Mock private UserDetailsView userDetailsView;
  private UserDetailsPresenter userDetailsPresenter;
  private HttpException mockHttpException;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    setUpMocks();
    LoadUserDetailsUseCase loadUserDetailsUseCase =
        new LoadUserDetailsUseCase(gitHubService, threadExecutor, postExecutionThread);
    userDetailsPresenter = new UserDetailsPresenter(loadUserDetailsUseCase);
    userDetailsPresenter.attachView(userDetailsView);
  }

  private void setUpMocks() {
    when(gitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME)).thenReturn(
        Observable.just(MockFactory.buildMockUserDetailsResponse()));
    mockHttpException = new HttpException(Response.error(404, null));
    when(gitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME_ERROR)).thenReturn(
        Observable.<List<Repository>>error(mockHttpException));
    when(gitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME_NO_RESULTS)).thenReturn(
        Observable.just(MockFactory.buildEmptyRepositoryList()));
    when(threadExecutor.getScheduler()).thenReturn(Schedulers.immediate());
    when(postExecutionThread.getScheduler()).thenReturn(Schedulers.immediate());
  }

  @Test
  public void loadRepositoriesShouldShowProgressIndicator() {
    userDetailsPresenter.loadRepositories(MockFactory.TEST_USERNAME);
    verify(userDetailsView).showProgressIndicator();
  }

  @Test
  public void loadRepositoriesShouldShowMessageIfEmptyResponse() {
    userDetailsPresenter.loadRepositories(MockFactory.TEST_USERNAME_NO_RESULTS);
    verify(userDetailsView).showMessage("The user does not have any public repositories");
  }

  @Test
  public void loadRepositoriesShouldShowMessageIfError() {
    userDetailsPresenter.loadRepositories(MockFactory.TEST_USERNAME_ERROR);
    verify(userDetailsView).showMessage("Error loading repositories");
  }

  @Test
  public void loadRepositoriesShouldHideProgressIndicatorOnComplete() {
    userDetailsPresenter.loadRepositories(MockFactory.TEST_USERNAME);
    verify(userDetailsView).hideProgressIndicator();
  }

  @Test
  public void loadRepositoriesShouldCallShowRepositoriesWhenReceivedResults() {
    userDetailsPresenter.loadRepositories(MockFactory.TEST_USERNAME);
    verify(userDetailsView).showRepositories(anyList());
  }

  @Test
  public void destroyShouldUnsubscribeIfTrue() {
    userDetailsPresenter = new UserDetailsPresenter(mockLoadUserDetailsUseCase);
    userDetailsPresenter.destroy(true);
    verify(mockLoadUserDetailsUseCase).unsubscribe();
  }

  @Test
  public void destroyShouldNotUnsubScribeIfFalse() {
    userDetailsPresenter = new UserDetailsPresenter(mockLoadUserDetailsUseCase);
    userDetailsPresenter.destroy(false);
    verifyZeroInteractions(mockLoadUserDetailsUseCase);
  }

  @After
  public void cleanUp() {
    userDetailsPresenter.detachView();
  }

}