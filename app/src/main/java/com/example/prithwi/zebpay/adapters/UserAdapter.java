package com.example.prithwi.zebpay.adapters;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.prithwi.zebpay.R;
import com.example.prithwi.zebpay.models.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prithwi on 28/03/18.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
     Context context;
    public ArrayList<Users> usersList;
    public ArrayList<Users> orig;



    public UserAdapter(Context context,ArrayList<Users> Ulist) {
        this.context=context;
        this.usersList = Ulist;

    }
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserAdapter.MyViewHolder holder, int position) {
        Users users = usersList.get(position);
        //Users users = filterList.get(position);
        holder.name.setText("Name: "+users.getName());
        holder.repo.setText("Reputation: "+users.getReputation());
        if(users.getImageUrl()!=null){
        Glide.with(context).load(users.getImageUrl()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {

        return usersList.size();
    }

    public Filter getFilter() {
        return new Filter()
        {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Users> results = new ArrayList<Users>();
                if (orig == null)
                    orig = usersList;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Users g : orig) {
                            if (g.getName().toLowerCase().contains(constraint.toString()))
                                results.add(g);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                usersList = (ArrayList<Users>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, repo;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tv_name);
            repo = (TextView) view.findViewById(R.id.tv_repo);
            imageView=(ImageView)view.findViewById(R.id.img_user);
        }
    }
}




