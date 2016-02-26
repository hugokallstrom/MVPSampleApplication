package com.hugo.mvpsampleapplication.utils.dependencyinjection.components;

import com.hugo.mvpsampleapplication.features.searchuser.SearchUserFragment;
import com.hugo.mvpsampleapplication.features.userdetails.UserDetailsFragment;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.PerActivity;
import com.hugo.mvpsampleapplication.utils.dependencyinjection.modules.UserModule;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = UserModule.class)
public interface UserComponent {
  void inject(SearchUserFragment searchUserFragment);
  void inject(UserDetailsFragment userDetailsFragment);
}
