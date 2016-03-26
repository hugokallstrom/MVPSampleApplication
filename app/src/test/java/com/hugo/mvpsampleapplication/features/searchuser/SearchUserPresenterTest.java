package com.hugo.mvpsampleapplication.features.searchuser;

import com.hugo.mvpsampleapplication.MockFactory;
import com.hugo.mvpsampleapplication.model.network.GitHubService;
import com.hugo.mvpsampleapplication.model.network.SearchResponse;
import com.hugo.mvpsampleapplication.utils.PostExecutionThread;
import com.hugo.mvpsampleapplication.utils.ThreadExecutor;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchUserPresenterTest {

  @Mock private GitHubService gitHubService;
  @Mock private ThreadExecutor threadExecutor;
  @Mock private PostExecutionThread postExecutionThread;
  @Mock private SearchUserUseCase mockSearchUserUseCase;

  @Mock private SearchUserView searchUserView;
  private SearchUserPresenter searchUserPresenter;
  private HttpException mockHttpException;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    setUpMocks();
    SearchUserUseCase searchUserUseCase =
        new SearchUserUseCase(gitHubService, threadExecutor, postExecutionThread);
    searchUserPresenter = new SearchUserPresenter(searchUserUseCase);
    searchUserPresenter.attachView(searchUserView);
  }

  private void setUpMocks() {
    when(gitHubService.searchUser(MockFactory.TEST_USERNAME)).thenReturn(
        Observable.just(MockFactory.buildMockSearchResponse()));
    mockHttpException = new HttpException(Response.error(404, null));
    when(gitHubService.searchUser(MockFactory.TEST_USERNAME_ERROR)).thenReturn(
        Observable.<SearchResponse>error(mockHttpException));
    when(gitHubService.searchUser(MockFactory.TEST_USERNAME_NO_RESULTS)).thenReturn(
        Observable.just(MockFactory.buildEmptyMockSearchResponse()));
    when(threadExecutor.getScheduler()).thenReturn(Schedulers.immediate());
    when(postExecutionThread.getScheduler()).thenReturn(Schedulers.immediate());
  }

  @Test
  public void loadUsersShouldShowProgressIndicator() {
    searchUserPresenter.loadUsers(MockFactory.TEST_USERNAME);
    verify(searchUserView, times(1)).showProgressIndicator();
  }

  @Test
  public void loadUsersShouldShowErrorMessageIfNoInput() {
    searchUserPresenter.loadUsers(null);
    verify(searchUserView).showMessage("Enter a username");
  }

  @Test
  public void loadUsersShouldShowErrorMessageIfEmptyInput() {
    searchUserPresenter.loadUsers("");
    verify(searchUserView).showMessage("Enter a username");
  }

  @Test
  public void loadUsersShouldDisplayMessageIfNoResults() {
    searchUserPresenter.loadUsers(MockFactory.TEST_USERNAME_NO_RESULTS);
    verify(searchUserView).showMessage("No results");
  }

  @Test
  public void loadUsersShouldAddUsersWhenReceivedResult() {
    searchUserPresenter.loadUsers(MockFactory.TEST_USERNAME);
    SearchResponse searchResponse = MockFactory.buildMockSearchResponse();
    verify(searchUserView).showUsers(anyList());
  }

  @Test
  public void loadUsersShouldDisplayMessageOnError() {
    searchUserPresenter.loadUsers(MockFactory.TEST_USERNAME_ERROR);
    verify(searchUserView).showMessage("Error loading repositories");
  }

  @Test
  public void loadUsersShouldHideProgressIndicatorAfterCompleted() {
    searchUserPresenter.loadUsers(MockFactory.TEST_USERNAME);
    verify(searchUserView).hideProgressIndicator();
  }

  @Test
  public void onUserClickShouldStartDetailsActivity() {
    searchUserPresenter.onUserClick(MockFactory.TEST_USERNAME);
    verify(searchUserView).startUserDetailsActivity(MockFactory.TEST_USERNAME);
  }

  @Test
  public void destroyShouldUnsubscribeIfTrue() {
    searchUserPresenter = new SearchUserPresenter(mockSearchUserUseCase);
    searchUserPresenter.destroy(true);
    verify(mockSearchUserUseCase).unsubscribe();
  }

  @Test
  public void destroyShouldNotUnsubScribeIfFalse() {
    searchUserPresenter = new SearchUserPresenter(mockSearchUserUseCase);
    searchUserPresenter.destroy(false);
    verifyZeroInteractions(mockSearchUserUseCase);
  }

  @After
  public void cleanUp() {
    searchUserPresenter.detachView();
  }
}