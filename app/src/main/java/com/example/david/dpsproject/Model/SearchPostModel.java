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
import com.example.david.dpsproject.AsyncTask.SearchPostGetMoreTask;
import com.example.david.dpsproject.AsyncTask.SearchPostTask;
import com.example.david.dpsproject.AsyncTask.getActualPostTask;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.SearchPost;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DefaultProgressBarPresenter;
import com.example.david.dpsproject.R;

import java.util.ArrayList;

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
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
    private View myView;
    private int position;
    private String SearchString;
    private Activity mActivity;
    private SwipeRefreshLayout refreshLayout;

    public SearchPostModel(DataBaseConnectionsPresenter dataBase, View view, String s, Activity activity){
        searchPosts = new ArrayList<>();
        posts = new ArrayList<>();
        dataBaseConnectionsPresenter =dataBase;
        position=0;
        myView=view;
        SearchString=s;
        mActivity=activity;
        listView =(ListView)myView.findViewById(R.id.postview);
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
                    getFirstView();
                }
                else{
                    getFirstView();
                }
            }
        });

    }

    public void setSearchPosts(ArrayList<SearchPost> s){
        try {
            searchPosts = s;
            setKey(searchPosts.get(searchPosts.size() - 1).getKey());
        }catch (Exception e){
            Toast.makeText(mActivity,"Nothing was found",Toast.LENGTH_SHORT).show();
        }
    }
    public void addSearchPost(ArrayList<SearchPost> search){
        for(SearchPost s:search){
            searchPosts.add(s);
        }
        setKey(searchPosts.get(search.size()-1).getKey());
    }

    public void getFirstView(){
        position=0;
        defaultProgressBarPresenter.showmProgressBarFooter();
        final SearchPostTask searchPostTask = new SearchPostTask(dataBaseConnectionsPresenter.getDbReference(),SearchString,this);
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

            final getActualPostTask searchPostTask = new getActualPostTask(dataBaseConnectionsPresenter.getDbReference(), defaultProgressBarPresenter,searchPosts,myPostAdapter,posts,position);
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
            defaultProgressBarPresenter.showmProgressBarFooter();
            final SearchPostGetMoreTask searchPostGetMoreTask = new SearchPostGetMoreTask(dataBaseConnectionsPresenter.getDbReference(),SearchString,this,getKey());
            searchPostGetMoreTask.execute();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(searchPostGetMoreTask.getStatus()== AsyncTask.Status.RUNNING){
                        searchPostGetMoreTask.cancel(true);
                        Toast.makeText(mActivity,"Nothing was found",Toast.LENGTH_SHORT).show();
                    }
                }
            },5000);
        }
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
