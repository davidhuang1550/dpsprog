package com.example.david.dpsproject.Model;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.example.david.dpsproject.Dialog.PleaseLogin;
import com.example.david.dpsproject.Fragments.CreatePost.CreatePost;
import com.example.david.dpsproject.Fragments.CreatePost.CreatePostImage;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;

/**
 * Created by david on 2017-01-07.
 */

public class FabModel  {
    private FloatingActionButton fab;
    private FloatingActionButton fab_image;
    private FloatingActionButton fab_desc;
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
    private Activity mActivity;

    public FabModel(FloatingActionButton f1, FloatingActionButton f2, FloatingActionButton f3, DataBaseConnectionsPresenter db, Activity activity){

        fab=f1;
        fab_image=f2;
        fab_desc=f3;
        dataBaseConnectionsPresenter=db;
        mActivity=activity;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseConnectionsPresenter.setFirebaseUser();
                if(dataBaseConnectionsPresenter.getFirebaseUser()!=null){ // show and hide compose
                    if(fab_image.getVisibility()==View.VISIBLE && fab_desc.getVisibility()==view.VISIBLE){
                        fab_desc.hide();
                        fab_image.hide();
                    }
                    else{
                        fab_desc.show();
                        fab_image.show();
                    }

                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString("Message","You must be logged in to create post");
                    PleaseLogin pleaseLogin = new PleaseLogin();
                    pleaseLogin.setArguments(bundle);
                    pleaseLogin.show(((navigation)mActivity).getFragmentManager(),"Alert Dialog Fragment");
                }



            }
        });
        fab_image.setOnClickListener(new View.OnClickListener(){ // go to create image
            @Override
            public void onClick(View view) {
                fab_desc.hide();
                fab_image.hide();
                CreatePostImage createPost = new CreatePostImage();
                FragmentTransaction transaction= ((navigation)mActivity).getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.enter_anim,R.animator.exit_anim,R.animator.enter_anim_back,R.animator.exit_anime_back);
                transaction.add(R.id.content_frame,createPost).addToBackStack("Posts").commit();
            }
        });
        fab_desc.setOnClickListener(new View.OnClickListener(){ // go to create desc
            public void onClick(View view) {
                fab_desc.hide();
                fab_image.hide();
                CreatePost createPost = new CreatePost();
                FragmentTransaction transaction= ((navigation)mActivity).getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.enter_anim,R.animator.exit_anim,R.animator.enter_anim_back,R.animator.exit_anime_back);
                transaction.add(R.id.content_frame,createPost).addToBackStack("Posts").commit();
            }
        });

    }
    public void showFab(){

        if(fab_image!=null)fab_image.hide();
        if(fab_desc!=null)fab_desc.hide();
        if(fab!=null)fab.show();
    }
    public void hideFab(){
        if(fab_image!=null)fab_image.hide();
        if(fab_desc!=null)fab_desc.hide();
        if(fab!=null)fab.hide();
    }

}
