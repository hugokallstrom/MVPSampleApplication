package com.hugo.mvpsampleapplication.features.searchuser;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import com.hugo.mvpsampleapplication.MockFactory;
import com.hugo.mvpsampleapplication.R;
import com.hugo.mvpsampleapplication.app.MVPApplication;
import com.hugo.mvpsampleapplication.model.network.GitHubService;
import com.hugo.mvpsampleapplication.model.network.SearchResponse;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.components.ApplicationComponent;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.modules.ApplicationModule;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import retrofit.HttpException;
import retrofit.Response;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.hugo.mvpsampleapplication.OrientationChangeAction.orientationLandscape;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchUserActivityTest {

  @Rule public DaggerMockRule<ApplicationComponent> daggerMockRule =
      new DaggerMockRule<>(ApplicationComponent.class, new ApplicationModule()).set(
          new DaggerMockRule.ComponentSetter<ApplicationComponent>() {
            @Override
            public void setComponent(ApplicationComponent applicationComponent) {
              getApp().setApplicationComponent(applicationComponent);
            }
          });

  @Rule public final ActivityTestRule<SearchUserActivity> activityTestRule =
      new ActivityTestRule<>(SearchUserActivity.class, false, false);

  @Mock GitHubService mockGitHubService;

  private MVPApplication getApp() {
    return (MVPApplication) InstrumentationRegistry.getInstrumentation()
        .getTargetContext()
        .getApplicationContext();
  }

  @Before
  public void setUp() {
    setUpMockGitHubService();
    activityTestRule.launchActivity(null);
  }

  @SuppressWarnings("unchecked")
  private void setUpMockGitHubService() {
    when(mockGitHubService.searchUser(MockFactory.TEST_USERNAME)).thenReturn(
        Observable.just(MockFactory.buildMockSearchResponse()));
    when(mockGitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME)).thenReturn(
        Observable.just(MockFactory.buildMockUserDetailsResponse()));
    when(mockGitHubService.searchUser(MockFactory.TEST_USERNAME_NO_RESULTS)).thenReturn(
        Observable.just(MockFactory.buildEmptyMockSearchResponse()));

    HttpException mockHttpException = new HttpException(Response.error(404, null));
    when(mockGitHubService.searchUser(MockFactory.TEST_USERNAME_ERROR)).thenReturn(
        Observable.<SearchResponse>error(mockHttpException));
  }

  @Test
  public void clickSearchWithNoQueryInputShouldReturnErrorMessage() throws Exception {
    onView(withId(R.id.button_search)).perform(click());
    onView(allOf(withId(android.support.design.R.id.snackbar_text),
        withText("Enter a username"))).check(matches(isDisplayed()));
  }

  @Test
  public void networkErrorShouldDisplayInSnackBar() throws Exception {
    performSearch(MockFactory.TEST_USERNAME_ERROR);
    onView(withId(android.support.design.R.id.snackbar_text)).check(
        matches(withText("Error loading users")));
  }

  @Test
  public void noUserErrorShouldDisplayInSnackBar() throws Exception {
    performSearch(MockFactory.TEST_USERNAME_NO_RESULTS);
    onView(withId(android.support.design.R.id.snackbar_text)).check(
        matches(withText("No users found")));
  }

  @Test
  public void searchResultsShouldBeHiddenIfNoSearch() throws Exception {
    onView(withId(R.id.search_user_list)).check(matches(not(isDisplayed())));
  }

  @Test
  public void clickSearchShouldDisplayResults() throws Exception {
    performSearch(MockFactory.TEST_USERNAME);
    onView(withId(R.id.search_user_list)).check(matches(isDisplayed()));
  }

  @Test
  public void clickOnSearchResultItemShouldDisplayUserDetails() throws Exception {
    performSearch(MockFactory.TEST_USERNAME);
    onView(withId(R.id.search_user_list)).perform(
        RecyclerViewActions.actionOnItemAtPosition(0, click()));
    onView(withText(MockFactory.TEST_REPOSITORY)).check(matches(isDisplayed()));
  }

  @Test
  public void searchQueryTextShouldPersistAfterOrientationChange() throws Exception {
    onView(withId(R.id.edit_text_username)).perform(typeText("test"));
    onView(isRoot()).perform(orientationLandscape());
    onView(withId(R.id.edit_text_username)).check(matches(withText("test")));
  }

  @Test
  public void searchResultsShouldPersistAfterOrientationChange() throws Exception {
    performSearch(MockFactory.TEST_USERNAME);
    onView(isRoot()).perform(orientationLandscape());
    onView(withId(R.id.search_user_list)).check(matches(isDisplayed()));
  }

  private void performSearch(String query) {
    onView(withId(R.id.edit_text_username)).perform(typeText(query));
    onView(withId(R.id.button_search)).perform(click());
  }
}