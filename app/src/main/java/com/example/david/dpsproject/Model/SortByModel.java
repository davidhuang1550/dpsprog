package com.example.david.dpsproject.Model;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.AsyncTask.SortByLoadMoreTask;
import com.example.david.dpsproject.AsyncTask.SortByTask;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DefaultProgressBarPresenter;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.ProgressBarPresenter;
import com.example.david.dpsproject.R;
import com.google.firebase.database.Query;

import java.util.ArrayList;

/**
 * Created by david on 2016-12-28.
 */

public class SortByModel
{

    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
    private String sub;
    private ArrayList<Post> posts;
    private String startKey;
    private Activity mActivity;
    private DefaultProgressBarPresenter defaultProgressBarPresenter;
    private ListView lview;
    private MyPostAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private View myView;
    private ProgressBarPresenter progressBarPresenter;
    int visibleItemCount;
    int totalItemCount;
    int firstVisibleItem;
    Query Query;
    String sortBy;
    public SortByModel(DataBaseConnectionsPresenter db, String s, Activity activity, ListView listView, View view){
        dataBaseConnectionsPresenter =db;
        sub=s;
        posts= new ArrayList<>();
        lview=listView;
        mActivity=activity;
        adapter = new MyPostAdapter(mActivity, posts);
        myView=view;
        defaultProgressBarPresenter= new DefaultProgressBarPresenter(mActivity,lview);
        progressBarPresenter = new ProgressBarPresenter(mActivity,listView);

        refreshLayout= (SwipeRefreshLayout)myView.findViewById(R.id.swiperefresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (lview != null){
                    final Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.splashfadeout);
                    lview.startAnimation(animation);

                    if(lview.getFooterViewsCount()==0){
                        HeaderViewListAdapter hlva = (HeaderViewListAdapter)lview.getAdapter();
                        MyPostAdapter tempAdapter = (MyPostAdapter) hlva.getWrappedAdapter();
                        if(tempAdapter!=null)tempAdapter.clearData();
                        tempAdapter.notifyDataSetChanged();
                    }else{
                        progressBarPresenter.hidemProgressBarFooter();
                        progressBarPresenter.hideErrorBar();
                        HeaderViewListAdapter hlva = (HeaderViewListAdapter)lview.getAdapter();
                        MyPostAdapter postAdapter = (MyPostAdapter) hlva.getWrappedAdapter();
                        postAdapter.clearData();
                        postAdapter.notifyDataSetChanged();
                    }


                    refreshLayout.setRefreshing(true);
                   sort();
                }
                else{
                    refreshLayout.setRefreshing(true);
                    sort();
                }


            }
        });
        lview.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(progressBarPresenter.getPin()==false) {
                        if(lview.getFooterViewsCount()==1){
                            progressBarPresenter.hideErrorBar();
                        }
                        addmore();
                    }
                }
            }

            public void onScroll(AbsListView view, int firstVisible,
                                 int visibleItem, int totalItem) {
                visibleItemCount=visibleItem;
                totalItemCount=totalItem;
                firstVisibleItem=firstVisible;

            }
        });

    }

    public void SortByYes(){
       Query= dataBaseConnectionsPresenter.getDbReference().child("Sub").child(sub).child("posts").orderByChild("yes").
                limitToLast(15);
        sortBy="yes";
    }
    public void SortByNo(){
        Query= dataBaseConnectionsPresenter.getDbReference().child("Sub").child(sub).child("posts").orderByChild("no").
                limitToLast(15);
        sortBy="no";

    }
    public void SortByResponse(){
        Query= dataBaseConnectionsPresenter.getDbReference().child("Sub").child(sub).child("posts").orderByChild("totalPost").
                limitToLast(15);
        sortBy="totalPost";
    }

    public void sort(){
        final Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.splashfadeout);
        lview.startAnimation(animation);

        lview.setAdapter(adapter);
        final SortByTask sortByTask = new SortByTask(dataBaseConnectionsPresenter.getDbReference(),sub,defaultProgressBarPresenter,Query,this);
        sortByTask.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sortByTask.getStatus()== AsyncTask.Status.RUNNING){
                    sortByTask.cancel(true);

                    Toast.makeText(mActivity,"Nothing was found",Toast.LENGTH_SHORT).show();
                }
            }
        },5000);
    }
    public void addmore(){

        Query = dataBaseConnectionsPresenter.getDbReference().child("Sub").child(sub).child("posts").orderByChild(sortBy).endAt(startKey).limitToLast(15);
        final SortByLoadMoreTask sortByTask = new SortByLoadMoreTask(dataBaseConnectionsPresenter.getDbReference(),sub,progressBarPresenter,Query,this,startKey);
        sortByTask.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sortByTask.getStatus()== AsyncTask.Status.RUNNING){
                    sortByTask.cancel(true);

                    Toast.makeText(mActivity,"Nothing was found",Toast.LENGTH_SHORT).show();
                }
            }
        },5000);
    }
    public void setPost(ArrayList<Post> p){
        Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.splashfadeoutleft);
        for(Post temp: p){
            posts.add(temp);
        }
        lview.startAnimation(animation);
        adapter.notifyDataSetChanged();
    }

    public void setStartKey(String key){
        startKey=key;
    }

}
