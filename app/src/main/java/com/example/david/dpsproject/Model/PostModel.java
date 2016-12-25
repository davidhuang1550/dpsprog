package com.example.david.dpsproject.Model;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Toast;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.AsyncTask.DefaultPostTask;
import com.example.david.dpsproject.AsyncTask.LoadDefaultPostTask;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Presenter.ProgressBarPresenter;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by david on 2016-12-20.
 */

public class PostModel {
    private ArrayList<Post> posts;
    private Activity mActivity;
    private DatabaseReference dbReference;
    private View myView;
    private SwipeRefreshLayout refreshLayout;
    private Users user;
    private FirebaseUser firebaseUser;
    private ProgressBarPresenter progressBarPresenter;
    private ArrayList<String> Category;

    public PostModel(Activity activity , DatabaseReference db, View view, SwipeRefreshLayout refresh, Users u, FirebaseUser fbu){
        posts =new ArrayList<Post>();
        mActivity=activity;
        dbReference=db;
        myView=view;
        refreshLayout=refresh;
        user=u;
        firebaseUser=fbu;
        Category= new ArrayList<>();
    }

    public void setDefaultPostView(){
        ArrayList<String> category= new ArrayList<String>();
        category.add("Jesus");
        category.add("Soccer");
        category.add("Uplifting");
        final LoadDefaultPostTask loadDefaultPostTask= new LoadDefaultPostTask(mActivity,dbReference,myView,refreshLayout,category);

        if(refreshLayout!=null){
            if(!refreshLayout.isRefreshing())((navigation) mActivity).ShowProgressDialog();
        }
        loadDefaultPostTask.execute();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loadDefaultPostTask.getStatus()== AsyncTask.Status.RUNNING){
                    loadDefaultPostTask.cancel(true);
                    ((navigation) mActivity).HideProgressDialog();
                    Toast.makeText(mActivity, "Connection too slow", Toast.LENGTH_SHORT).show();

                }
            }
        },5000);
    }
    public void setCategory(String cat){
        Category.clear();
        Category.add(cat);
    }
    public void setSearchView(){
        final LoadDefaultPostTask loadDefaultPostTask= new LoadDefaultPostTask(mActivity,dbReference,myView,refreshLayout,Category);

        if(refreshLayout!=null){
            if(!refreshLayout.isRefreshing())((navigation) mActivity).ShowProgressDialog();
        }
        loadDefaultPostTask.execute();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loadDefaultPostTask.getStatus()== AsyncTask.Status.RUNNING){
                    loadDefaultPostTask.cancel(true);
                    ((navigation) mActivity).HideProgressDialog();
                    Toast.makeText(mActivity, "Connection too slow", Toast.LENGTH_SHORT).show();

                }
            }
        },5000);
    }
    public void setPostView(){

        user= ((navigation)mActivity).getworkingUser();
        final LoadDefaultPostTask loadPostTask= new LoadDefaultPostTask(mActivity,dbReference,myView,refreshLayout, user.getSubcategory());
        loadPostTask.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loadPostTask.getStatus()==AsyncTask.Status.RUNNING){
                    if(firebaseUser!=null) {
                        loadPostTask.cancel(true);
                        ((navigation) mActivity).HideProgressDialog();
                        Toast.makeText(mActivity, "Connection too slow", Toast.LENGTH_SHORT).show();

                    }else{

                        setDefaultPostView();
                    }
                }
            }
        },5000);
    }
    public void setProgressBarPresenter(ProgressBarPresenter progressBar){
        progressBarPresenter=progressBar;
    }
    public void addmoreItems() {
        try{
            MyPostAdapter adapter = ((navigation) mActivity).getFrontPageAdapater();
            ArrayList<Post> posts = ((navigation) mActivity).getPostId();
            ArrayList<Post> currPost = ((navigation) mActivity).getCurrentList();
            int tempNum = ((navigation) mActivity).getCurrListPos();
            if ((posts.size() - 10) >= tempNum) {
                for (int i = tempNum; i < tempNum + 10; i++) {
                    currPost.add(posts.get(i));
                }
                adapter.notifyDataSetChanged();
                ((navigation) mActivity).setCurrListPos(tempNum + 10);
                progressBarPresenter.hidemProgressBarFooter();
            } else {
                if (user != null) {
                    final DefaultPostTask getMorePost = new DefaultPostTask(mActivity, dbReference, user.getSubcategory(), ((navigation) mActivity).getTimestamp(), progressBarPresenter);
                    getMorePost.execute();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (getMorePost.getStatus() == AsyncTask.Status.RUNNING) {
                                getMorePost.cancel(true);
                                progressBarPresenter.hidemProgressBarFooter();
                                progressBarPresenter.showErrorBar();


                            }
                        }
                    }, 5000);
                } else {
                    ArrayList<String> sub = new ArrayList<String>();
                    sub.add("Jesus");
                    sub.add("Soccer");
                    sub.add("Uplifting");
                    final DefaultPostTask getMorePost = new DefaultPostTask(mActivity, dbReference, sub, ((navigation) mActivity).getTimestamp(), progressBarPresenter);
                    getMorePost.execute();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (getMorePost.getStatus() == AsyncTask.Status.RUNNING) {
                                getMorePost.cancel(true);
                                progressBarPresenter.hidemProgressBarFooter();
                                progressBarPresenter.showErrorBar();


                            }
                        }
                    }, 5000);
                }

            }
        }catch (NullPointerException e){
            Toast.makeText(mActivity,"Something went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

}
