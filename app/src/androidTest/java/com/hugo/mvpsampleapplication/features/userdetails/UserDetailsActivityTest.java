package com.hugo.mvpsampleapplication.features.userdetails;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import com.hugo.mvpsampleapplication.MockFactory;
import com.hugo.mvpsampleapplication.app.MVPApplication;
import com.hugo.mvpsampleapplication.model.network.GitHubService;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.components.ApplicationComponent;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.modules.ApplicationModule;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.hugo.mvpsampleapplication.OrientationChangeAction.orientationLandscape;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserDetailsActivityTest {

  @Rule public DaggerMockRule<ApplicationComponent> daggerMockRule =
      new DaggerMockRule<>(ApplicationComponent.class, new ApplicationModule()).set(
          new DaggerMockRule.ComponentSetter<ApplicationComponent>() {
            @Override
            public void setComponent(ApplicationComponent applicationComponent) {
              getApp().setApplicationComponent(applicationComponent);
            }
          });

  @Rule public final ActivityTestRule<UserDetailsActivity> activityTestRule =
      new ActivityTestRule<>(UserDetailsActivity.class, false, false);

  @Mock GitHubService mockGitHubService;

  private MVPApplication getApp() {
    return (MVPApplication) InstrumentationRegistry.getInstrumentation()
        .getTargetContext()
        .getApplicationContext();
  }

  @Before
  public void setUp() {
    setUpMockGitHubService();
  }

  @SuppressWarnings("unchecked")
  private void setUpMockGitHubService() {
    when(mockGitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME)).thenReturn(
        Observable.just(MockFactory.buildMockUserDetailsResponse()));
    Observable error = Observable.error(new Throwable("Error"));
    when(mockGitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME_ERROR)).thenReturn(
        error);
    when(
        mockGitHubService.getRepositoriesFromUser(MockFactory.TEST_USERNAME_NO_RESULTS)).thenReturn(
        Observable.just(MockFactory.buildEmptyRepositoryList()));
  }

  @Test
  public void shouldDisplayRepositories() throws Exception {
    activityTestRule.launchActivity(buildIntent(MockFactory.TEST_USERNAME));
    onView(withText(MockFactory.TEST_REPOSITORY)).check(matches(isDisplayed()));
  }

  @Test
  public void shouldDisplayRepositoriesAfterConfigurationChange() throws Exception {
    activityTestRule.launchActivity(buildIntent(MockFactory.TEST_USERNAME));
    onView(isRoot()).perform(orientationLandscape());
    onView(withText(MockFactory.TEST_REPOSITORY)).check(matches(isDisplayed()));
  }

  @Test
  public void shouldDisplayNetworkErrorInSnackBar() throws Exception {
    activityTestRule.launchActivity(buildIntent(MockFactory.TEST_USERNAME_ERROR));
    onView(withId(android.support.design.R.id.snackbar_text)).check(matches(withText("Error loading repositories")));
  }

  @Test
  public void shouldDisplayEmptyRepositoriesErrorInSnackBar() throws Exception {
    activityTestRule.launchActivity(buildIntent(MockFactory.TEST_USERNAME_NO_RESULTS));
    onView(withId(android.support.design.R.id.snackbar_text)).check(matches(withText("No public repositories")));
  }

  private Intent buildIntent(String username) {
    Intent intent = new Intent();
    intent.putExtra("USERNAME", username);
    return intent;
  }
}