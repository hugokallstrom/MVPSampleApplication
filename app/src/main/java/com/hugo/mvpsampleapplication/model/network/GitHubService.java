package com.hugo.mvpsampleapplication.model.network;

import com.hugo.mvpsampleapplication.model.entities.Repository;

import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface GitHubService {

    String endpoint = "https://api.github.com/";
    String getRepoUrl = "users/{username}/repos";
    String searchUserUrl = "search/users";

    @GET(getRepoUrl)
    Observable<List<Repository>> getRepositoriesFromUser(@Path("username") String username);

    @GET(searchUserUrl)
    Observable<SearchResponse> searchUser(@Query("q") String username);

    class Factory {
        public static GitHubService create() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(endpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(GitHubService.class);
        }
    }
}
