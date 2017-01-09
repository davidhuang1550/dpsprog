package com.example.david.dpsproject.Fragments.ViewPost;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.dpsproject.Fragments.Authentication.LogIn;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;

/**
 * Created by david on 2016-12-22.
 */

public class LoginPostView extends Fragment implements View.OnClickListener{
    private Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.loginbutton, container, false);

        final TextView  textView = (TextView)myView.findViewById(R.id.Loginnow);
        textView.setOnClickListener(this);

        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    textView.setBackgroundResource(R.color.backgroundblue);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                   textView.setBackgroundResource(R.color.backgroundbluetouch);
                }
                return false;
            }
        });

        return myView;
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.Loginnow){
            FragmentManager fragmentManager = mActivity.getFragmentManager();
            ((navigation) mActivity).onBackPressed();
            fragmentManager.beginTransaction().replace(R.id.content_frame,new LogIn()).commit();
        }
    }
}
