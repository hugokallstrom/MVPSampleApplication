package com.hugo.mvpsampleapplication.view.activity;

import android.os.Bundle;
import android.util.Log;

import com.hugo.mvpsampleapplication.R;
import com.hugo.mvpsampleapplication.view.fragment.UserDetailsFragment;

public class UserDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        String username = getIntent().getStringExtra("username");
        if(getFragmentManager().findFragmentById(R.id.content_activity_user_details) == null) {
            addFragment(R.id.content_activity_user_details, UserDetailsFragment.newInstance(username));
        }
    }

}
