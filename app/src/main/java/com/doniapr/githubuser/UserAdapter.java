package com.doniapr.githubuser;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.doniapr.githubuser.DetailActivity.EXTRA_USER;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private ArrayList<User> usersFull;
    private ArrayList<User> users = new ArrayList<>();

    public void setData(ArrayList<User> users) {
        this.users = users;
        usersFull = new ArrayList<>(users);
        notifyDataSetChanged();
    }

    public void addUser(ArrayList<User> items){
        usersFull.addAll(items);
        users.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int i) {
        holder.txtName.setText(users.get(i).getName());
        Glide.with(holder.itemView.getContext())
                .load(users.get(i).getAvatar())
                .into(holder.imgAvatar);
        final User u = new User();
        u.setId(users.get(i).getId());
        u.setName(users.get(i).getName());
        u.setAvatar(users.get(i).getAvatar());
        u.setUrl(users.get(i).getUrl());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), DetailActivity.class);
                i.putExtra(EXTRA_USER, u);
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    private final Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<User> userFilter = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                userFilter.addAll(usersFull);
            }else {
                String filterPatern = constraint.toString().toLowerCase().trim();
                for (User user:usersFull){
                    if (user.getName().toLowerCase().contains(filterPatern)){
                        userFilter.add(user);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = userFilter;

            return  results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            users.clear();
            users.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public Filter getSearchFilter() {
        return searchFilter;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtName;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            txtName = itemView.findViewById(R.id.txt_name);
        }
    }
}
