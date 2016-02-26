package com.hugo.mvpsampleapplication.features.userdetails;

import com.hugo.mvpsampleapplication.features.UseCase;
import com.hugo.mvpsampleapplication.model.network.GitHubService;

import com.hugo.mvpsampleapplication.utils.PostExecutionThread;
import com.hugo.mvpsampleapplication.utils.ThreadExecutor;
import javax.inject.Inject;
import rx.Observable;

public class LoadUserDetailsUseCase extends UseCase {


    @Inject
    public LoadUserDetailsUseCase(GitHubService gitHubService, ThreadExecutor threadExecutor,
        PostExecutionThread postExecutionThread) {
        super(gitHubService, threadExecutor, postExecutionThread);
    }

    @Override public Observable buildUseCase(String username) throws NullPointerException {
        if (username == null) {
            throw new NullPointerException("Username must not be null");
        }
        return getGitHubService().getRepositoriesFromUser(username);
    }
}
