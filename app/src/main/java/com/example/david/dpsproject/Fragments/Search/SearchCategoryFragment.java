package com.example.david.dpsproject.Fragments.Search;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.PostPresenter;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.ProgressBarPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;

/**
 * Created by xlhuang3 on 11/8/2016.
 */
public class SearchCategoryFragment extends Fragment {
    private Activity mActivity;
    private View myView;
    private SwipeRefreshLayout refreshLayout;
    private ListView listView;
    private String Sub;
    private Users users;
    private PostPresenter postPresenter;
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
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
        ((navigation)mActivity).showFab();
        ((navigation)mActivity).setCategoryView(myView);
        ((navigation)mActivity).ShowSort();
        dataBaseConnectionsPresenter = ((navigation)mActivity).getDataBaseConnectionsPresenter();

        listView = (ListView)myView.findViewById(R.id.postview);
        refreshLayout = (SwipeRefreshLayout)myView.findViewById(R.id.swiperefresh);
        postPresenter = new PostPresenter(mActivity , dataBaseConnectionsPresenter, myView, refreshLayout, users);
        ProgressBarPresenter progressBarPresenter= new ProgressBarPresenter(mActivity, listView);
        postPresenter.setProgressBarPresenter(progressBarPresenter);

        final Bundle b = getArguments();
        if(b!=null) {
            ((navigation)mActivity).setSubCat(b.getString("Sub"));
            postPresenter.setCategory(b.getString("Sub"));
            Sub = b.getString("Sub");
            mActivity.setTitle(Sub);
            postPresenter.enableDisplayBySearch();
            postPresenter.setSearchPost();
            setSubscribe();



        }


        return myView;
    }
    public void onDestroy() {
        ViewGroup container = (ViewGroup)mActivity.findViewById(R.id.content_frame);
        container.removeAllViews();
        ((navigation)mActivity).HideSort();
        super.onDestroy();
    }

}
