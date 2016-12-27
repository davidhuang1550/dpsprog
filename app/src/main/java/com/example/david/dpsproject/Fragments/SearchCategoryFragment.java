package com.example.david.dpsproject.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.dpsproject.Adapters.MyPostAdapter;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Presenter.PostPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xlhuang3 on 11/8/2016.
 */
public class SearchCategoryFragment extends Fragment {
    private Activity mActivity;
    private FirebaseAuth authentication;
    private View myView;
    private FirebaseUser firebaseUser;
    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference dbReference;
    private ListView listView;
    private String Sub;
    private Users users;
    private PostPresenter postPresenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    public void setSubscribe(){
        users= ((navigation)mActivity).getworkingUser();
        boolean showsubscribe=false;
        if(users!=null){
            for(String s:users.getSubcategory()){
                if(s.equals(Sub)){
                    ((navigation)mActivity).showUnsubscribe();
                    showsubscribe=true;
                    break;
                }
            }
            if(!showsubscribe)((navigation)mActivity).showSubscribe();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.searchpage,container,false);
        authentication= FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference(); // access to database
        firebaseUser = authentication.getCurrentUser();
        listView = (ListView)myView.findViewById(R.id.listView);
        refreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swiperefresh);
        postPresenter = new PostPresenter(mActivity , dbReference, myView, refreshLayout, users, firebaseUser);

        final Bundle b = getArguments();
        if(b!=null) {
            ((navigation) mActivity).ShowProgressDialog();
            ((navigation)mActivity).setSubCat(b.getString("Sub"));
            postPresenter.setCategory(b.getString("Sub"));
            Sub = b.getString("Sub");
            mActivity.setTitle(Sub);
            postPresenter.setSearchPost();
            setSubscribe();



        }
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (listView != null){
                    final Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.splashfadeout);
                    listView.startAnimation(animation);
                    MyPostAdapter tempAdapter= (MyPostAdapter)listView.getAdapter();
                    tempAdapter.clearData();
                    tempAdapter.notifyDataSetChanged();
                    refreshLayout.setRefreshing(true);
                    postPresenter.setSearchPost();
                }
                else{
                    refreshLayout.setRefreshing(true);
                    postPresenter.setSearchPost();
                }


            }
        });

        return myView;
    }
    public void onDestroy() {
        ViewGroup container = (ViewGroup)mActivity.findViewById(R.id.content_frame);
        container.removeAllViews();
        super.onDestroy();
    }

}
