package com.hugo.mvpsampleapplication.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import com.hugo.mvpsampleapplication.R;
import com.hugo.mvpsampleapplication.model.entities.User;
import com.hugo.mvpsampleapplication.presenter.SearchUserPresenter;
import com.hugo.mvpsampleapplication.view.activity.UserDetailsActivity;
import com.hugo.mvpsampleapplication.view.adapter.UserListAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchUserFragment extends Fragment implements SearchUserView {

    private final String TAG = getClass().getSimpleName();

    private static final String SEARCH_USER_LIST_VISIBILITY = "searchUserListVisibility";
    private static final String PROGRESS_VISIBILITY = "progressVisibility";

    @Bind(R.id.search_user_list)
    RecyclerView userList;
    @Bind(R.id.progress_indicator)
    RelativeLayout progressIndicator;
    @Bind(R.id.button_search)
    ImageButton searchButton;
    @Bind(R.id.edit_text_username)
    EditText editTextUsername;

    private SearchUserPresenter searchUserPresenter;
    private UserListAdapter userListAdapter;

    public SearchUserFragment() {}

    public static SearchUserFragment newInstance() {
        return new SearchUserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_search_user, container, false);
        ButterKnife.bind(this, fragmentView);
        if(savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
        setPresenter();
        setUpUserListAdapter();
        setSearchButtonOnClickListener();
        setEditTextActionListener();
        userList.setLayoutManager(new LinearLayoutManager(getActivity()));
        userList.setAdapter(userListAdapter);
        return fragmentView;
    }

    private void setPresenter() {
        if(searchUserPresenter == null) {
            searchUserPresenter = new SearchUserPresenter();
            searchUserPresenter.attachView(this);
        }
    }

    private void setUpUserListAdapter() {
        if(userListAdapter == null) {
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
        if (username.isEmpty()) {
            showMessage("Enter a username");
        } else {
            hideKeyBoard(editTextUsername);
            searchUserPresenter.loadUsers(username);
        }
    }

    private void hideKeyBoard(View view) {
        view.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
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
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        searchUserPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }

}
