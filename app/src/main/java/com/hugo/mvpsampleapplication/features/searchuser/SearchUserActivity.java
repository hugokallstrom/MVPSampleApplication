package com.hugo.mvpsampleapplication.features.searchuser;

import android.os.Bundle;

import android.util.Log;
import com.hugo.mvpsampleapplication.R;
import com.hugo.mvpsampleapplication.features.BaseActivity;
import com.hugo.mvpsampleapplication.features.userdetails.UserDetailsActivity;
import com.hugo.mvpsampleapplication.features.userdetails.UserDetailsFragment;

public class SearchUserActivity extends BaseActivity
    implements SearchUserFragment.ActivityListener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_user);
    if (savedInstanceState == null) {
      addFragment(R.id.content_activity_search_user, SearchUserFragment.newInstance());
    }
  }

  @Override
  public void startUserDetails(String username) {
    UserDetailsFragment userDetailsFragment =
        (UserDetailsFragment) getSupportFragmentManager().findFragmentById(
            R.id.userDetailsFragment);
    if (userDetailsFragment == null) {
      Log.d("activity", "starting user details " + username);
      startActivity(UserDetailsActivity.newIntent(this, username));
    } else {
      userDetailsFragment.loadRepositories(username);
    }
  }
}
