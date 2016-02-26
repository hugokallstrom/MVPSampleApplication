package com.hugo.mvpsampleapplication.utils.dependencyinjection.modules;


import com.hugo.mvpsampleapplication.features.UseCase;
import com.hugo.mvpsampleapplication.features.searchuser.SearchUserUseCase;
import com.hugo.mvpsampleapplication.features.userdetails.LoadUserDetailsUseCase;
import com.hugo.mvpsampleapplication.model.network.GitHubService;
import com.hugo.mvpsampleapplication.utils.PostExecutionThread;
import com.hugo.mvpsampleapplication.utils.ThreadExecutor;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.PerActivity;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module
public class UserModule {

  public UserModule() {
  }

  @Provides
  @PerActivity
  @Named("searchUser")
  public UseCase provideSearchUserUseCase(GitHubService gitHubService, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    return new SearchUserUseCase(gitHubService, threadExecutor, postExecutionThread);
  }

  @Provides
  @PerActivity
  @Named("userDetails")
  public UseCase provideUserDetailsUseCase(GitHubService gitHubService, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    return new LoadUserDetailsUseCase(gitHubService, threadExecutor, postExecutionThread);
  }
}
