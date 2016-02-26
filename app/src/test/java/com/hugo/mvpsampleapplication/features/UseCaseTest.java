package com.hugo.mvpsampleapplication.features;

import com.hugo.mvpsampleapplication.MockFactory;
import com.hugo.mvpsampleapplication.model.network.GitHubService;
import com.hugo.mvpsampleapplication.model.network.SearchResponse;
import com.hugo.mvpsampleapplication.utils.PostExecutionThread;
import com.hugo.mvpsampleapplication.utils.ThreadExecutor;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;
import rx.Subscription;
import rx.observers.TestSubscriber;
import rx.schedulers.Schedulers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by hugo on 2/25/16.
 */
public class UseCaseTest {
  @Mock
  private GitHubService gitHubService;
  @Mock
  private PostExecutionThread mockPostExecutionThread;
  @Mock
  private ThreadExecutor mockThreadExecutor;

  private UseCase useCase;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(mockPostExecutionThread.getScheduler()).thenReturn(Schedulers.immediate());
    when(mockThreadExecutor.getScheduler()).thenReturn(Schedulers.immediate());
    useCase = new UseCase(gitHubService, mockThreadExecutor, mockPostExecutionThread) {
      @Override public Observable buildUseCase(String query) {
        return Observable.just(MockFactory.buildMockSearchResponse());
      }
    };
  }

  @Test
  public void buildUseCaseShouldReturnObservable() throws Exception {
    Observable observable = useCase.buildUseCase(MockFactory.TEST_USERNAME);
    assertNotNull(observable);
  }
  @Test
  public void getGitHubServiceShouldNotReturnNull() throws Exception {
    GitHubService gitHubService = useCase.getGitHubService();
    assertNotNull(gitHubService);
  }

  @Test
  public void unsubcribeShouldUnsubscribeSubscription() throws Exception {
    TestSubscriber<SearchResponse> testSubscriber = new TestSubscriber<>();
    useCase.execute(testSubscriber, MockFactory.TEST_USERNAME);
    useCase.unsubscribe();
    Subscription subscription = useCase.getSubscription();
    assertEquals(true, subscription.isUnsubscribed());
  }

}