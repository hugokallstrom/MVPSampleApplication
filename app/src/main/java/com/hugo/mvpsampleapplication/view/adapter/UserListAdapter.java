package com.hugo.mvpsampleapplication.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hugo.mvpsampleapplication.R;
import com.hugo.mvpsampleapplication.model.entities.User;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String username);
    }

    private List<User> users;
    private OnItemClickListener onItemClickListener;

    public UserListAdapter() {
        this.users = Collections.emptyList();
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        final UserViewHolder viewHolder = new UserViewHolder(itemView);
        View content = viewHolder.contentLayout;
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(viewHolder.getUsername());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = users.get(position);
        Context context = holder.usernameTextView.getContext();
        holder.usernameTextView.setText(user.getLogin());
        Picasso.with(context)
                .load(user.getAvatarUrl())
                .fit()
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        View contentLayout;
        TextView usernameTextView;
        CircleImageView avatar;

        public UserViewHolder(View itemView) {
            super(itemView);
            contentLayout = itemView.findViewById(R.id.layout_content);
            usernameTextView = (TextView) itemView.findViewById(R.id.item_username);
            avatar = (CircleImageView) itemView.findViewById(R.id.avatar);
        }

        public String getUsername() {
            return usernameTextView.getText().toString();
        }

    }

}
