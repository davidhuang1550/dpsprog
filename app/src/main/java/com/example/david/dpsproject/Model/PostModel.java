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
import com.example.david.dpsproject.AsyncTask.DefaultPostTask;
import com.example.david.dpsproject.AsyncTask.LoadDefaultPostTask;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DefaultProgressBarPresenter;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.ProgressBarPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;

import java.util.ArrayList;

/**
 * Created by david on 2016-12-20.
 */

public class PostModel {
    private Activity mActivity;
    private View myView;
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
    private SwipeRefreshLayout refreshLayout;
    private Users user;
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
    private Boolean DisplayBySearch;
    private int lastvisibleItem;
    int visibleItemCount;
    int totalItemCount;
    int firstVisibleItem;
    public PostModel(Activity activity , DataBaseConnectionsPresenter dataBase, View view, SwipeRefreshLayout refresh, Users u){
        mActivity=activity;
        myView=view;
        refreshLayout=refresh;
        user=u;
        p= new ArrayList<>();
        Category= new ArrayList<>();
        listView = (ListView) myView.findViewById(R.id.postview);
        adapter = new MyPostAdapter(mActivity, p);
        listView.setAdapter(adapter);
        defaultProgressBarPresenter = new DefaultProgressBarPresenter(mActivity,listView);
        postId= new ArrayList<Post>();
        DisplayBySearch=false;
        dataBaseConnectionsPresenter =dataBase;


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (listView != null){
                    final Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.splashfadeout);
                    listView.startAnimation(animation);

                    if(listView.getFooterViewsCount()==0){
                        try {
                            HeaderViewListAdapter hlva = (HeaderViewListAdapter) listView.getAdapter();
                            MyPostAdapter tempAdapter = (MyPostAdapter) hlva.getWrappedAdapter();
                            if (tempAdapter != null) tempAdapter.clearData();
                            tempAdapter.notifyDataSetChanged();
                        }catch(ClassCastException e){
                            MyPostAdapter tempAdapter = (MyPostAdapter)listView.getAdapter();
                            if (tempAdapter != null) tempAdapter.clearData();
                            tempAdapter.notifyDataSetChanged();
                        }
                    }else{
                        progressBarPresenter.hidemProgressBarFooter();
                        progressBarPresenter.hideErrorBar();
                        HeaderViewListAdapter hlva = (HeaderViewListAdapter)listView.getAdapter();
                        MyPostAdapter postAdapter = (MyPostAdapter) hlva.getWrappedAdapter();
                        postAdapter.clearData();
                        postAdapter.notifyDataSetChanged();
                    }


                    refreshLayout.setRefreshing(true);
                    if(user!=null&&DisplayBySearch==false)  setPostView();
                    else setDefaultPostView();
                }
                else{
                    refreshLayout.setRefreshing(true);
                    if(user!=null&&DisplayBySearch==false)  setPostView();
                    else setDefaultPostView();
                }


            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (firstVisibleItem > lastvisibleItem) {
                    ((navigation)mActivity).hideStatusBar();
                    ((navigation)mActivity).hideFab();
                } else if (firstVisibleItem < lastvisibleItem) {
                    ((navigation)mActivity).showStatusBar();
                    ((navigation)mActivity).showFab();
                }

                lastvisibleItem = firstVisibleItem;


                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(progressBarPresenter.getPin()==false) {
                        if(listView.getFooterViewsCount()==1){
                            progressBarPresenter.hideErrorBar();
                        }
                        progressBarPresenter.showmProgressBarFooter();
                        addmoreItems();
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

    public void setDefaultPostView(){
        setCategoryDefault();
        final LoadDefaultPostTask loadDefaultPostTask= new LoadDefaultPostTask(mActivity, dataBaseConnectionsPresenter.getDbReference(),
                myView,refreshLayout, Category,defaultProgressBarPresenter,listView,adapter,p,this);

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
    public void enableDisplayBySearch(){
        DisplayBySearch=true;
    }
    public void setUserCategory(){
        Category=user.getSubcategory();
    }
    public void setCategory(String cat){
        Category.clear();
        Category.add(cat);
    }
    public void setCategoryDefault(){
        Category.clear();
        Category.add("Jesus");
        Category.add("Soccer");
        Category.add("Uplifting");
    }
    public void setSearchView(){
        final LoadDefaultPostTask loadDefaultPostTask= new LoadDefaultPostTask(mActivity, dataBaseConnectionsPresenter.getDbReference(),
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
        setUserCategory();
        final LoadDefaultPostTask loadDefaultPostTask= new LoadDefaultPostTask(mActivity, dataBaseConnectionsPresenter.getDbReference(),
                myView,refreshLayout,Category,defaultProgressBarPresenter,listView,adapter,p,this);

        if(refreshLayout!=null){
            if(!refreshLayout.isRefreshing())defaultProgressBarPresenter.showmProgressBarFooter();
        }

        loadDefaultPostTask.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loadDefaultPostTask.getStatus()==AsyncTask.Status.RUNNING){
                    if(dataBaseConnectionsPresenter.getFirebaseUser()!=null) {
                        loadDefaultPostTask.cancel(true);
                        defaultProgressBarPresenter.hidemProgressBarFooter();
                        ((navigation)mActivity).HideProgressDialog();
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
                    final DefaultPostTask getMorePost = new DefaultPostTask(mActivity, dataBaseConnectionsPresenter.getDbReference(), Category, getTimestamp(), progressBarPresenter,this);
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
        }catch (NullPointerException e){
            Toast.makeText(mActivity,"Something went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

}
