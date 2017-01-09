package com.example.david.dpsproject.Adapters;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Dialog.PleaseLogin;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.Fragments.ViewPost.postview;
import com.example.david.dpsproject.navigation;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by david on 2016-11-03.
 */
public class MyPostAdapter extends BaseAdapter {
    private ArrayList<Post> posts;
    private Context mActivity;
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;

   public MyPostAdapter(Activity Activity, ArrayList<Post> p){
       posts=p;
       mActivity=Activity;
       dataBaseConnectionsPresenter = ((navigation)mActivity).getDataBaseConnectionsPresenter();
    }
    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public void clearData(){

        posts.clear();

    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View row;
        final LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.postlist,null);

        TextView tView = (TextView)row.findViewById(R.id.PostT);
        TextView subcatView = (TextView)row.findViewById(R.id.sub_cat_view);
        TextView TimeStamp =(TextView)row.findViewById(R.id.timestamp);
        final ImageView bookmark = (ImageView)row.findViewById(R.id.bookmark);


        tView.setText(posts.get(i).getTitle());
        if(posts.get(i).getTimestamp()!=null){
            long temp = (System.currentTimeMillis()/1000)-(posts.get(i).getTimestamp());

            if(temp<60)TimeStamp.setText(temp+" Second(s) ago");
            else if((temp/60)<60)TimeStamp.setText(temp/60+" Minute(s) ago");
            else if((temp/60)/60<24) TimeStamp.setText((temp/60)/60+" Hour(s) ago");
            else if(temp/60/60/24<31)TimeStamp.setText((temp/60)/60/24+" Day(s) ago");
            else if(temp/60/60/24/30<13)TimeStamp.setText((temp/60)/60/24/30+" Month(s) ago");
            else TimeStamp.setText((temp/60)/60/24/30/12+" Year(s) ago");

        }
        subcatView.setText(posts.get(i).getSubN());

        if(((navigation)mActivity).getworkingUser()!=null){
            Users users= ((navigation)mActivity).getworkingUser();
            if (users.findBookMark(posts.get(i).getSubN(),posts.get(i).getKey())) {
                bookmark.setImageResource(R.drawable.bookmarkchecked);
            }

        }

        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataBaseConnectionsPresenter.setFirebaseUser();
                if(dataBaseConnectionsPresenter.getFirebaseUser()!=null){
                    Users users= ((navigation)mActivity).getworkingUser();
                    ArrayList<String> templist;
                    if(bookmark.getDrawable().getConstantState()== mActivity.getResources().getDrawable(R.drawable.bookmarkchecked).getConstantState()){
                        bookmark.setImageResource(R.drawable.bookmarkunchecked);
                        templist=  users.removeFromBookMarked(posts.get(i).getSubN(),posts.get(i).getKey());
                    }
                    else{
                        bookmark.setImageResource(R.drawable.bookmarkchecked);
                        templist=  users.AddToBookMarked(posts.get(i).getSubN(),posts.get(i).getKey());

                    }
                    dataBaseConnectionsPresenter.getDbReference().child("Users").child(dataBaseConnectionsPresenter.getUID()).
                            child("Bookmarks").child(posts.get(i).getSubN()).setValue(templist);

                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString("Message","You must be logged in to bookmark");
                    PleaseLogin pleaseLogin = new PleaseLogin();
                    pleaseLogin.setArguments(bundle);
                    pleaseLogin.show(((Activity)mActivity).getFragmentManager(),"Alert Dialog Fragment");
                }

            }
        });

        row.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();

                postview pV = new postview();
                bundle.putSerializable("Post_Object", (Serializable) posts.get(i));
                bundle.putString("UID",posts.get(i).getPosterId());
                pV.setArguments(bundle);

                FragmentTransaction transaction= ((navigation)mActivity).getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.enter_anim,R.animator.exit_anim,R.animator.enter_anim_back,R.animator.exit_anime_back);
                transaction.add(R.id.content_frame,pV).addToBackStack("Posts").commit();

            }
        });
        return row;

    }
}
