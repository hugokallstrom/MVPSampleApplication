package com.hugo.mvpsampleapplication.features.userdetails;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hugo.mvpsampleapplication.R;
import com.hugo.mvpsampleapplication.features.BaseActivity;
import com.hugo.mvpsampleapplication.model.entities.Repository;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import javax.inject.Inject;

/**
 * UserDetailsFragment is a passive view where all user interactions is passed to
 * UserDetailsPresenter. UserDetailsFragment implements UserDetailsView in order to receive calls
 * from UserDetailsPresenter.
 */
public class UserDetailsFragment extends Fragment implements UserDetailsView {

  @Bind(R.id.progress_indicator) RelativeLayout progressIndicator;
  @Bind(R.id.repositories_list) RecyclerView repositoriesList;

  @Inject UserDetailsPresenter userDetailsPresenter;
  private RepositoriesAdapter userDetailsAdapter;
  private LinearLayoutManager layoutManager;

  public UserDetailsFragment() {
  }

  public static UserDetailsFragment newInstance(String username) {
    UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
    Bundle bundle = new Bundle();
    bundle.putString("username", username);
    userDetailsFragment.setArguments(bundle);
    return userDetailsFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    ((BaseActivity) getActivity()).getUserComponent().inject(this);
    userDetailsAdapter = new RepositoriesAdapter();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    layoutManager = new LinearLayoutManager(context);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View fragmentView = inflater.inflate(R.layout.fragment_user_details, container, false);
    ButterKnife.bind(this, fragmentView);
    userDetailsPresenter.attachView(this);
    repositoriesList.setAdapter(userDetailsAdapter);
    repositoriesList.setLayoutManager(layoutManager);
    if (getArguments() != null && savedInstanceState == null) {
      String username = getArguments().getString("username");
      loadRepositories(username);
    }
    return fragmentView;
  }

  public void loadRepositories(String username) {
    userDetailsPresenter.loadRepositories(username);
  }

  @Override
  public void showMessage(String message) {
    View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
    Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
    snackbar.show();
  }

  @Override
  public void showProgressIndicator() {
    repositoriesList.setVisibility(View.INVISIBLE);
    progressIndicator.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgressIndicator() {
    progressIndicator.setVisibility(View.INVISIBLE);
    repositoriesList.setVisibility(View.VISIBLE);
  }

  @Override
  public void showRepositories(List<Repository> repositories) {
    userDetailsAdapter.setReposetories(repositories);
    userDetailsAdapter.notifyDataSetChanged();
  }

  @Override
  public void onDetach() {
    super.onDetach();
    layoutManager = null;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
    userDetailsPresenter.destroy(false);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    userDetailsPresenter.destroy(true);
  }
}
