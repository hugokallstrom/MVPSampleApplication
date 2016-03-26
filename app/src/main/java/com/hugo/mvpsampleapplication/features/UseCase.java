package com.hugo.mvpsampleapplication.features;

import com.hugo.mvpsampleapplication.model.network.GitHubService;
import com.hugo.mvpsampleapplication.utils.PostExecutionThread;
import com.hugo.mvpsampleapplication.utils.ThreadExecutor;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public abstract class UseCase {

  private final GitHubService gitHubService;
  private final ThreadExecutor threadExecutor;
  private final PostExecutionThread postExecutionThread;
  private Subscription subscription = Subscriptions.empty();

  public UseCase(GitHubService gitHubService, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    this.gitHubService = gitHubService;
    this.threadExecutor = threadExecutor;
    this.postExecutionThread = postExecutionThread;
  }

  public GitHubService getGitHubService() {
    return gitHubService;
  }

  public Subscription getSubscription() {
    return subscription;
  }

  public abstract Observable buildUseCase(String query);

  @SuppressWarnings("unchecked") public void execute(Subscriber useCaseSubscriber, String query) {
    subscription = buildUseCase(query)
        .observeOn(postExecutionThread.getScheduler())
        .subscribeOn(threadExecutor.getScheduler())
        .subscribe(useCaseSubscriber);
  }

  public void unsubscribe() {
    if (subscription != null && !subscription.isUnsubscribed()) {
      subscription.unsubscribe();
    }
  }
}

