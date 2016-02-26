package com.hugo.mvpsampleapplication.utils.dependencyinjection.components;

import com.hugo.mvpsampleapplication.features.BaseActivity;
import com.hugo.mvpsampleapplication.model.network.GitHubService;
import com.hugo.mvpsampleapplication.utils.PostExecutionThread;
import com.hugo.mvpsampleapplication.utils.ThreadExecutor;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.modules.ApplicationModule;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(BaseActivity baseActivity);

  GitHubService gitHubService();
  ThreadExecutor threadExecutor();
  PostExecutionThread postExecutionThread();

}
