package com.hugo.mvpsampleapplication.features.searchuser;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.hugo.mvpsampleapplication.R;
import com.hugo.mvpsampleapplication.features.BaseActivity;
import com.hugo.mvpsampleapplication.model.entities.User;
import java.util.List;
import javax.inject.Inject;

public class SearchUserFragment extends Fragment implements SearchUserView {

  private static final String SEARCH_USER_LIST_VISIBILITY = "searchUserListVisibility";
  private static final String PROGRESS_VISIBILITY = "progressVisibility";

  @Bind(R.id.search_user_list) RecyclerView userList;
  @Bind(R.id.progress_indicator) RelativeLayout progressIndicator;
  @Bind(R.id.button_search) ImageButton searchButton;
  @Bind(R.id.edit_text_username) EditText editTextUsername;

  @Inject SearchUserPresenter searchUserPresenter;
  private UserListAdapter userListAdapter;
  private ActivityListener activityListener;
  private LinearLayoutManager layoutManager;

  public interface ActivityListener {
    void startUserDetails(String username);
  }

  public SearchUserFragment() {

  }

  public static SearchUserFragment newInstance() {
    return new SearchUserFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
    ((BaseActivity) getActivity()).getUserComponent().inject(this);
    setUpUserListAdapter();
  }

  private void setUpUserListAdapter() {
    userListAdapter = new UserListAdapter();
    userListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(String username) {
        if (searchUserPresenter != null && !username.isEmpty()) {
          searchUserPresenter.onUserClick(username);
        }
      }
    });
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof ActivityListener) {
      this.activityListener = (ActivityListener) context;
    }
    layoutManager = new LinearLayoutManager(context);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View fragmentView = inflater.inflate(R.layout.fragment_search_user, container, false);
    ButterKnife.bind(this, fragmentView);
    if (savedInstanceState != null) {
      restoreState(savedInstanceState);
    }
    setSearchButtonOnClickListener();
    setEditTextActionListener();
    searchUserPresenter.attachView(this);
    userList.setLayoutManager(layoutManager);
    userList.setAdapter(userListAdapter);
    return fragmentView;
  }

  public void setSearchButtonOnClickListener() {
    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        searchUsers();
      }
    });
  }

  private void setEditTextActionListener() {
    editTextUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          searchUsers();
          return true;
        }
        return false;
      }
    });
  }

  public void searchUsers() {
    String username = editTextUsername.getText().toString();
    hideKeyBoard(editTextUsername);
    searchUserPresenter.loadUsers(username);
  }

  private void hideKeyBoard(View view) {
    view.clearFocus();
    InputMethodManager mgr =
        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  @Override
  public void showMessage(String message) {
    View rootView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
    Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
    snackbar.show();
  }

  @Override
  public void showProgressIndicator() {
    userList.setVisibility(View.INVISIBLE);
    progressIndicator.setVisibility(View.VISIBLE);
  }

  @Override
  public void hideProgressIndicator() {
    progressIndicator.setVisibility(View.INVISIBLE);
    userList.setVisibility(View.VISIBLE);
  }

  @Override
  public void showUsers(List<User> users) {
    userListAdapter.setUsers(users);
    userListAdapter.notifyDataSetChanged();
  }

  @Override
  public void startUserDetailsActivity(String username) {
    activityListener.startUserDetails(username);
  }

  @SuppressWarnings("ResourceType")
  private void restoreState(Bundle savedInstanceState) {
    progressIndicator.setVisibility(savedInstanceState.getInt(PROGRESS_VISIBILITY));
    userList.setVisibility(savedInstanceState.getInt(SEARCH_USER_LIST_VISIBILITY));
  }

  @Override
  public void onSaveInstanceState(Bundle state) {
    super.onSaveInstanceState(state);
    state.putInt(PROGRESS_VISIBILITY, progressIndicator.getVisibility());
    state.putInt(SEARCH_USER_LIST_VISIBILITY, userList.getVisibility());
  }

  @Override
  public void onDetach() {
    super.onDetach();
    activityListener = null;
    layoutManager = null;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.unbind(this);
    searchUserPresenter.destroy(false);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    searchUserPresenter.destroy(true);
  }
}
