package com.example.david.dpsproject.Fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.squareup.picasso.Picasso;

/**
 * Created by david on 2016-12-22.
 */

public class ZoomInFragment extends Fragment implements View.OnClickListener{

    private Activity mActivity;
    private View myView;
    private Uri uri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity= getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.zoominlayout, container, false);
        Bundle bundle = getArguments();
        ImageView imageView = (ImageView)myView.findViewById(R.id.zoomedImage);



        if(bundle!=null){
            uri = Uri.parse(bundle.getString("URI"));
            Picasso.with(mActivity).load(uri).into(imageView);

        }
        imageView.setOnClickListener(this);

        return myView;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.zoomedImage){
            FragmentManager fragmentManager = mActivity.getFragmentManager();

            fragmentManager.beginTransaction().remove(this).commit();
        }
    }
}
