package com.example.prithwi.zebpay.activities;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.android.volley.toolbox.Volley;
import com.example.prithwi.zebpay.R;
import com.example.prithwi.zebpay.adapters.UserAdapter;
import com.example.prithwi.zebpay.models.Users;
import com.example.prithwi.zebpay.network.ZapPayHttpComm;
import com.example.prithwi.zebpay.network.ZapPayHttpCommCallback;
import com.example.prithwi.zebpay.utils.DBHelper;
import com.example.prithwi.zebpay.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ZapPayHttpCommCallback {
    SearchView mSearchView;
    private Users uList;
    private ArrayList<Users> usersList = new ArrayList<>();
    Users users;
    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;
    JSONObject jsonObject;
    LinearLayoutManager manager;
    boolean isLoading = false;
    private int currentItem,scrollOutItem,totalItems=0;
    DBHelper mydb;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.volleyRequestQueue = Volley.newRequestQueue(this);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.mydb = new DBHelper(this);
        manager=new LinearLayoutManager(this);


        networkCall();



        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(manager);

        mAdapter = new UserAdapter(this, usersList);
        mRecyclerView.setAdapter(mAdapter);

       // setupSearchView();
        final android.widget.Filter filter = mAdapter.getFilter();

        this.mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.isEmpty()) {
                    usersList.clear();
                    isLoading=false;
                    networkCall();


                } else {
                    filter.filter(newText);
                }
                return true;
            }
        });



        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(isNetworkAvailable()){

                isLoading=true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem=manager.getChildCount();
                totalItems=manager.getItemCount();
                scrollOutItem=manager.findFirstVisibleItemPosition();

                if(isLoading &&(currentItem+scrollOutItem==totalItems)){
                    isLoading=false;
                    mAdapter.usersList.clear();
                    refresh();
                }
            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();


    }

    private void networkCall(){

        if(isNetworkAvailable()){
        //Utils.getInstance().displayLoading(this);
        ZapPayHttpComm.getNewInstance(this).callUserService(1,10);
        }
        else{
            Cursor res = mydb.getallData();
            if (res.getCount() != 0) {
                while (res.moveToNext()) {

                    JSONObject jobj=new JSONObject();


                    try {
                        jobj.put("userName",res.getString(0));
                        jobj.put("reputation",res.getString(1));
                        jobj.put("image",res.getString(2));

                        this.uList=new Users(jobj);



                            this.usersList.add(uList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(mAdapter!=null){
                this.mAdapter.notifyDataSetChanged();}

            }
        }
    }
    private void refresh(){
        Utils.getInstance().displayLoading(this);
        ZapPayHttpComm.getNewInstance(this).callUserService(2,20);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(boolean status, int tag, JSONObject jsonResponse) {
        Utils.getInstance().hideLoading();
        if (tag == ZapPayHttpComm.USER_SERVICE) {

            if (jsonResponse != null) {

                if (status) {

                    JSONArray data = jsonResponse.optJSONArray("items");
                    if (data.length() > 0) {
                        for (int i = 0; i < data.length(); i++) {
                            this.jsonObject = data.optJSONObject(i);
                            this.users = new Users(jsonObject);
                            this.usersList.add(users);
                            insertIntoDb();
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                    else {
                        Utils.alert(ZapPayHttpComm.GENERIC_ERROR_MESSAGE, this);
                    }
                } else {
                    Utils.displayServerErrorMessage(jsonResponse, this);

                }
            } else {
                Utils.alert(ZapPayHttpComm.GENERIC_ERROR_MESSAGE, this);

            }
        }
    }




    @Override
    public void onFailure(JSONObject error, int tag) {
        Utils.getInstance().hideLoading();

    }

    private void insertIntoDb() {

        if (this.users != null) {
            boolean isInserted = mydb.insertData(users.getName(), users.getReputation(),users.getImageUrl());

        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
