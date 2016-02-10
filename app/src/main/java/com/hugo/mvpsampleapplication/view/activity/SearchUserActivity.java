package com.hugo.mvpsampleapplication.view.activity;

import android.os.Bundle;

import com.hugo.mvpsampleapplication.R;
import com.hugo.mvpsampleapplication.view.fragment.SearchUserFragment;

public class SearchUserActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        if(getFragmentManager().findFragmentById(R.id.content_activity_search_user) == null) {
            addFragment(R.id.content_activity_search_user, SearchUserFragment.newInstance());
        }
    }

}
