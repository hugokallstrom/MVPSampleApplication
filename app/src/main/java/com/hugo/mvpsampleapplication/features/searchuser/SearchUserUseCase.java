package com.hugo.mvpsampleapplication.features.searchuser;

import android.content.Context;

import com.hugo.mvpsampleapplication.app.MVPApplication;
import com.hugo.mvpsampleapplication.features.UseCase;
import com.hugo.mvpsampleapplication.model.network.GitHubService;

import com.hugo.mvpsampleapplication.utils.PostExecutionThread;
import com.hugo.mvpsampleapplication.utils.ThreadExecutor;
import javax.inject.Inject;
import rx.Observable;

public class SearchUserUseCase extends UseCase {

  @Inject
  public SearchUserUseCase(GitHubService gitHubService, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(gitHubService, threadExecutor, postExecutionThread);
  }

  @Override
  public Observable buildUseCase(String username) throws NullPointerException {
    if (username == null) {
      throw new NullPointerException("Query must not be null");
    }
    return getGitHubService().searchUser(username);
  }
}
