package com.example.david.dpsproject.Model;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.AsyncTask.SearchPostTask;
import com.example.david.dpsproject.AsyncTask.getActualPostTask;
import com.example.david.dpsproject.AsyncTask.getUserPostTask;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.SearchPost;
import com.example.david.dpsproject.Presenter.DefaultProgressBarPresenter;
import com.example.david.dpsproject.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 2016-12-26.
 */

public class SearchPostModel {
    private String Key;
    private ArrayList<SearchPost> searchPosts;
    private ListView listView;
    private DefaultProgressBarPresenter defaultProgressBarPresenter;
    private ArrayList<Post> posts;
    private MyPostAdapter myPostAdapter;
    private DatabaseReference databaseReference;
    private View myView;
    private int position;
    private String SearchString;
    private Activity mActivity;
    private SwipeRefreshLayout refreshLayout;

    public SearchPostModel(DatabaseReference db,View view,String s,Activity activity){
        searchPosts = new ArrayList<>();
        posts = new ArrayList<>();
        databaseReference=db;
        position=0;
        myView=view;
        SearchString=s;
        mActivity=activity;
        listView =(ListView)myView.findViewById(R.id.listView);
        myPostAdapter = new MyPostAdapter(mActivity,posts);
        listView.setAdapter(myPostAdapter);
        defaultProgressBarPresenter = new DefaultProgressBarPresenter(mActivity,listView);

        refreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swiperefresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (listView != null){
                    final Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.splashfadeout);
                    listView.startAnimation(animation);
                    myPostAdapter.clearData();
                    myPostAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(true);
                    getFirstView();
                }
                else{
                    refreshLayout.setRefreshing(true);
                    getFirstView();
                }
            }
        });
    }

    public void setSearchPosts(ArrayList<SearchPost> s){
        searchPosts=s;
        setKey(searchPosts.get(searchPosts.size()-1).getKey());
    }
    public void addSearchPost(ArrayList<SearchPost> search){
        for(SearchPost s:search){
            searchPosts.add(s);
        }
        Key=searchPosts.get(search.size()-1).getKey();
    }

    public void getFirstView(){
        defaultProgressBarPresenter.showmProgressBarFooter();
        final SearchPostTask searchPostTask = new SearchPostTask(databaseReference,SearchString,mActivity,listView,this);
        searchPostTask.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(searchPostTask.getStatus()== AsyncTask.Status.RUNNING){
                    searchPostTask.cancel(true);

                    Toast.makeText(mActivity,"Nothing was found",Toast.LENGTH_SHORT).show();
                }
            }
        },5000);
    }
    public void setSearchString(String s){
        SearchString=s;
    }

    public void LoadMore(){
        if(searchPosts.size()>=position){

            final getActualPostTask searchPostTask = new getActualPostTask(databaseReference, defaultProgressBarPresenter,searchPosts,myPostAdapter,posts,position);
            searchPostTask.execute();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(searchPostTask.getStatus()== AsyncTask.Status.RUNNING){
                        searchPostTask.cancel(true);

                        Toast.makeText(mActivity,"Nothing was found",Toast.LENGTH_SHORT).show();
                    }
                }
            },5000);
            position+=10;
        }
        else{

        }
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
