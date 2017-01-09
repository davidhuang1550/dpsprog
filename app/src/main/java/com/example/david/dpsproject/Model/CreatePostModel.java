package com.example.david.dpsproject.Model;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.example.david.dpsproject.AsyncTask.CreatePostTask;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;

/**
 * Created by david on 2017-01-02.
 */

public class CreatePostModel {

    private Post post;
    private Activity mActivity;
    private String sub_cat;
    private Users users;
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
    private Fragment fragment;
    private ProgressDialog pDialog;
    public CreatePostModel(Post p, Activity activity, DataBaseConnectionsPresenter dataBase, String sub, Users u, Fragment frag){
        mActivity=activity;
        post=p;
        users=u;
        sub_cat=sub;
        dataBaseConnectionsPresenter =dataBase;
        fragment=frag;
    }
    public void ShowProgressDialog() { // progress
        if (pDialog == null) {
            pDialog = new ProgressDialog(mActivity);
            pDialog.setMessage("Creating Posts");
            pDialog.setIndeterminate(true);
        }
        pDialog.show();
    }
    public void HideProgressDialog() {
        if(pDialog!=null && pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
    public void createPost(){
        ShowProgressDialog();
        final CreatePostTask loadDefaultPostTask= new CreatePostTask(mActivity, dataBaseConnectionsPresenter.getDbReference(),fragment,sub_cat, post,
                users, dataBaseConnectionsPresenter.getFirebaseUser(),pDialog);
        loadDefaultPostTask.execute();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(loadDefaultPostTask.getStatus()== AsyncTask.Status.RUNNING){
                    HideProgressDialog();
                    Toast.makeText(mActivity,"Something went wrong",Toast.LENGTH_SHORT).show();
                }
            }
        },10000);
    }

}
