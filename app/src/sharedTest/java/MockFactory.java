package com.hugo.mvpsampleapplication;

import com.hugo.mvpsampleapplication.model.entities.Repository;
import com.hugo.mvpsampleapplication.model.entities.User;
import com.hugo.mvpsampleapplication.model.network.SearchResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 2/15/16.
 */
public class MockFactory {

  public static final String TEST_USERNAME = "Tester";
  public static final String TEST_REPOSITORY = "TestRepo";
  public static final String TEST_USERNAME_NO_RESULTS = "TesterNoResults";
  public static final String TEST_USERNAME_ERROR = "TesterError";
  private static final String TEST_REPO_URL = "http://repo.url";
  private static final String TEST_AVATAR_URL = "http://avatar.url";
  private static final String TEST_LANGUAGE = "Java";
  private static final String TEST_DESCRIPTION = "Test Description";

  public static SearchResponse buildMockSearchResponse() {
    User testUser = new User();
    testUser.setLogin(TEST_USERNAME);
    ArrayList<User> users = new ArrayList<>();
    users.add(testUser);
    SearchResponse searchResponse = new SearchResponse();
    searchResponse.setUsers(users);
    return searchResponse;
  }

  public static List<Repository> buildMockUserDetailsResponse() {
    Repository repository = new Repository();
    repository.setId(1);
    repository.setStars(2);
    repository.setForks(3);
    repository.setWatchers(4);
    repository.setDescription("Test Repository");
    repository.setName(TEST_REPOSITORY);
    repository.setLanguage("Java");
    ArrayList<Repository> repositories = new ArrayList<>();
    repositories.add(repository);
    return repositories;
  }

  public static User buildMockUser() {
    User user = new User();
    user.setId(1);
    user.setLogin(TEST_USERNAME);
    user.setReposUrl(TEST_REPO_URL);
    user.setAvatarUrl(TEST_AVATAR_URL);
    return user;
  }

  public static Repository buildMockRepository() {
    Repository repository = new Repository();
    repository.setId(1);
    repository.setLanguage(TEST_LANGUAGE);
    repository.setName(TEST_USERNAME);
    repository.setDescription(TEST_DESCRIPTION);
    repository.setForks(1);
    repository.setStars(2);
    repository.setWatchers(3);
    return repository;
  }

  public static SearchResponse buildEmptyMockSearchResponse() {
    ArrayList<User> users = new ArrayList<>(0);
    SearchResponse searchResponse = new SearchResponse();
    searchResponse.setUsers(users);
    return searchResponse;
  }

  public static List<Repository> buildEmptyRepositoryList() {
    ArrayList<Repository> repositories = new ArrayList<>();
    return repositories;
  }
}
