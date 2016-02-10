package com.hugo.mvpsampleapplication.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hugo.mvpsampleapplication.R;
import com.hugo.mvpsampleapplication.model.entities.Repository;
import com.hugo.mvpsampleapplication.presenter.UserDetailsPresenter;
import com.hugo.mvpsampleapplication.view.adapter.ReposetoriesAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class UserDetailsFragment extends Fragment implements UserDetailsView {

    @Bind(R.id.progress_indicator)
    RelativeLayout progressIndicator;
    @Bind(R.id.reposetories_list)
    RecyclerView reposetoriesList;

    private ReposetoriesAdapter userDetailsAdapter;
    private UserDetailsPresenter userDetailsPresenter;
    private UserDetailsListener userDetailsListener;
    private String username;

    public UserDetailsFragment() {}

    public static UserDetailsFragment newInstance(String username) {
        UserDetailsFragment userDetailsFragment = new UserDetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        userDetailsFragment.setArguments(bundle);
        return userDetailsFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.username = getArguments().getString("username");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter();
    }

    private void setPresenter() {
        userDetailsPresenter = new UserDetailsPresenter();
        userDetailsPresenter.attachView(this);
    }

    private void loadReposetories() {
        userDetailsPresenter.loadReposetories(username);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_user_details, container, false);
        ButterKnife.bind(this, fragmentView);
        username = getArguments().getString("username", "hugokallstrom");
        setupReposetoriesList(reposetoriesList);
        loadReposetories();
        return fragmentView;
    }

    public void setupReposetoriesList(RecyclerView reposetoriesList) {
        userDetailsAdapter = new ReposetoriesAdapter();
        reposetoriesList.setAdapter(userDetailsAdapter);
        reposetoriesList.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserDetailsListener) {
            userDetailsListener = (UserDetailsListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        userDetailsListener = null;
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressIndicator() {
        progressIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressIndicator() {
        progressIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showRepositories(List<Repository> repositories) {
        reposetoriesList.setVisibility(View.VISIBLE);
        userDetailsAdapter.setReposetories(repositories);
        userDetailsAdapter.notifyDataSetChanged();

    }

    public interface UserDetailsListener {
    }
}
