package com.example.david.dpsproject.Model;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.AsyncTask.DefaultPostTask;
import com.example.david.dpsproject.AsyncTask.LoadDefaultPostTask;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Presenter.DefaultProgressBarPresenter;
import com.example.david.dpsproject.Presenter.ProgressBarPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by david on 2016-12-20.
 */

public class PostModel {
    private Activity mActivity;
    private DatabaseReference dbReference;
    private View myView;
    private SwipeRefreshLayout refreshLayout;
    private Users user;
    private FirebaseUser firebaseUser;
    private ProgressBarPresenter progressBarPresenter;
    private ArrayList<String> Category;
    private DefaultProgressBarPresenter defaultProgressBarPresenter;
    private ListView listView;
    private MyPostAdapter adapter;
    private ArrayList<Post> p;
    private long timestampfrom;
    private ArrayList<Post> InList;
    private ArrayList<Post> postId;
    private int currListPos;
    public PostModel(Activity activity , DatabaseReference db, View view, SwipeRefreshLayout refresh, Users u, FirebaseUser fbu){
        mActivity=activity;
        dbReference=db;
        myView=view;
        refreshLayout=refresh;
        user=u;
        firebaseUser=fbu;
        p= new ArrayList<>();
        Category= new ArrayList<>();
        listView = (ListView) myView.findViewById(R.id.postview);
        adapter = new MyPostAdapter(mActivity, p);
        listView.setAdapter(adapter);
        defaultProgressBarPresenter = new DefaultProgressBarPresenter(mActivity,listView);
        postId= new ArrayList<Post>();
    }

    public void setDefaultPostView(){
        ArrayList<String> category= new ArrayList<String>();
        category.add("Jesus");
        category.add("Soccer");
        category.add("Uplifting");
        final LoadDefaultPostTask loadDefaultPostTask= new LoadDefaultPostTask(mActivity,dbReference,
                myView,refreshLayout,category,defaultProgressBarPresenter,listView,adapter,p,this);

        if(refreshLayout!=null){
            if(!refreshLayout.isRefreshing())defaultProgressBarPresenter.showmProgressBarFooter();
        }
        loadDefaultPostTask.execute();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loadDefaultPostTask.getStatus()== AsyncTask.Status.RUNNING){
                    loadDefaultPostTask.cancel(true);
                    defaultProgressBarPresenter.hidemProgressBarFooter();
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
        final LoadDefaultPostTask loadDefaultPostTask= new LoadDefaultPostTask(mActivity,dbReference,
                myView,refreshLayout,Category,defaultProgressBarPresenter,listView,adapter,p,this);

        if(refreshLayout!=null){
            if(!refreshLayout.isRefreshing())defaultProgressBarPresenter.showmProgressBarFooter();
        }
        loadDefaultPostTask.execute();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loadDefaultPostTask.getStatus()== AsyncTask.Status.RUNNING){
                    loadDefaultPostTask.cancel(true);
                    defaultProgressBarPresenter.hidemProgressBarFooter();
                    Toast.makeText(mActivity, "Connection too slow", Toast.LENGTH_SHORT).show();

                }
            }
        },5000);
    }
    public void setPostView(){

        user= ((navigation)mActivity).getworkingUser();
        final LoadDefaultPostTask loadDefaultPostTask= new LoadDefaultPostTask(mActivity,dbReference,
                myView,refreshLayout,user.getSubcategory(),defaultProgressBarPresenter,listView,adapter,p,this);
        loadDefaultPostTask.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loadDefaultPostTask.getStatus()==AsyncTask.Status.RUNNING){
                    if(firebaseUser!=null) {
                        loadDefaultPostTask.cancel(true);
                        defaultProgressBarPresenter.hidemProgressBarFooter();
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
    public void setPostId(ArrayList<Post> s){
        postId=s;
    }
    public ArrayList<Post> getPostId(){
        return postId;
    }
    public void setTimestamp(long time){
        timestampfrom=time;
    }
    public long getTimestamp(){
        return timestampfrom;
    }

    public void setCurrentList(ArrayList<Post> p , int currlist){
        InList=p;
        currListPos=currlist;
    }
    public void setCurrListPos(int num){
        currListPos=num;
    }

    public int getCurrListPos(){
        return currListPos;
    }
    public void addtoposts(ArrayList<Post> posts){
        for(int i=0;i<10;i++)InList.add(posts.get(i));
        for(Post p: posts){
            postId.add(p);
        }
        adapter.notifyDataSetChanged();
        progressBarPresenter = new ProgressBarPresenter(mActivity,listView);
        progressBarPresenter.hidemProgressBarFooter();

    }
    public ArrayList<Post> getCurrentList(){
        return InList;
    }
    public void addmoreItems() {
        try{

            int tempNum = getCurrListPos();
            if ((postId.size() - 10) >= tempNum) {
                for (int i = tempNum; i < tempNum + 10; i++) {
                    InList.add(postId.get(i));
                }
                adapter.notifyDataSetChanged();
                setCurrListPos(tempNum + 10);
                progressBarPresenter.hidemProgressBarFooter();
            } else {
                if (user != null) {
                    final DefaultPostTask getMorePost = new DefaultPostTask(mActivity, dbReference, user.getSubcategory(), getTimestamp(), progressBarPresenter,this);
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
                    final DefaultPostTask getMorePost = new DefaultPostTask(mActivity, dbReference, sub, getTimestamp(), progressBarPresenter,this);
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
