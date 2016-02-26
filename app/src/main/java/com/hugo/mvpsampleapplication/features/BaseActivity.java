package com.hugo.mvpsampleapplication.features;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import com.hugo.mvpsampleapplication.app.MVPApplication;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.components.ApplicationComponent;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.components.DaggerUserComponent;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.components.UserComponent;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.modules.UserModule;

/**
 * Created by hugo on 1/22/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

  private UserComponent userComponent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getApplicationComponent().inject(this);
    initializeInject();
  }

  protected void addFragment(int containerId, Fragment fragment) {
    FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
    fragmentTransaction.add(containerId, fragment);
    fragmentTransaction.commit();
  }

  public ApplicationComponent getApplicationComponent() {
    return ((MVPApplication) getApplication()).getApplicationComponent();
  }

  private void initializeInject() {
    userComponent = DaggerUserComponent.builder()
        .applicationComponent(getApplicationComponent())
        .userModule(new UserModule())
        .build();
  }

  public UserComponent getUserComponent() {
    return userComponent;
  }
}
