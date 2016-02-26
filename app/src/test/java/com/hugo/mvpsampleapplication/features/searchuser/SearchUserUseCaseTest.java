package com.hugo.mvpsampleapplication.features.searchuser;

import com.hugo.mvpsampleapplication.MockFactory;
import com.hugo.mvpsampleapplication.model.entities.User;
import com.hugo.mvpsampleapplication.model.network.GitHubService;
import com.hugo.mvpsampleapplication.model.network.SearchResponse;
import com.hugo.mvpsampleapplication.utils.PostExecutionThread;
import com.hugo.mvpsampleapplication.utils.ThreadExecutor;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import rx.Observable;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hugo on 2/25/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchUserUseCaseTest {
  @Mock
  private GitHubService gitHubService;
  @Mock
  private PostExecutionThread mockPostExecutionThread;
  @Mock
  private ThreadExecutor mockThreadExecutor;

  private SearchUserUseCase searchUserUseCase;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(mockPostExecutionThread.getScheduler()).thenReturn(Schedulers.immediate());
    when(mockThreadExecutor.getScheduler()).thenReturn(Schedulers.immediate());
    SearchResponse searchResponse = MockFactory.buildMockSearchResponse();
    when(gitHubService.searchUser(any(String.class))).thenReturn(Observable.just(searchResponse));
    searchUserUseCase = new SearchUserUseCase(gitHubService, mockThreadExecutor, mockPostExecutionThread);
  }

  @Test
  public void buildUseCaseShouldCallSearchUser() throws Exception {
    searchUserUseCase.buildUseCase(MockFactory.TEST_USERNAME);
    verify(gitHubService).searchUser(MockFactory.TEST_USERNAME);
  }

  @Test(expected = NullPointerException.class)
  public void buildUseCaseShouldThrowNullPointerExceptionIfQueryNotSet() throws Exception {
    searchUserUseCase.buildUseCase(null);
  }

  @Test
  public void executeShouldReturnOneSearchResponse() {
    TestSubscriber<SearchResponse> testSubscriber = new TestSubscriber<>();
    searchUserUseCase.execute(testSubscriber, MockFactory.TEST_USERNAME);
    testSubscriber.assertCompleted();
    testSubscriber.assertNoErrors();
    List<SearchResponse> searchResponseList = testSubscriber.getOnNextEvents();
    Assert.assertEquals(1, searchResponseList.size());
  }

  @Test
  public void executeShouldReturnSearchResponseWithUser() {
    TestSubscriber<SearchResponse> testSubscriber = new TestSubscriber<>();
    searchUserUseCase.execute(testSubscriber, MockFactory.TEST_USERNAME);
    testSubscriber.assertCompleted();
    testSubscriber.assertNoErrors();
    List<SearchResponse> searchResponseList = testSubscriber.getOnNextEvents();
    List<User> users = searchResponseList.get(0).getUsers();
    Assert.assertEquals(1, users.size());
  }
}