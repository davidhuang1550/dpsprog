package com.example.david.dpsproject.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.dpsproject.AsyncTask.ProfileTask;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by david on 2016-11-16.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private View myView;
    private TextView History;
    private TextView Bookmarks;
    private Button Upload;

    FirebaseAuth authentication;
    DatabaseReference dbReference;
    FirebaseUser firebaseUser;

    ArrayList<Post> ptemp;
    private ListView listView;
    private Activity mActivity;
    ProgressDialog pDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity= getActivity();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.profile_fragment,container,false);
        mActivity.setTitle("Profile");

        History = (TextView) myView.findViewById(R.id.Tposts);
        Bookmarks = (TextView) myView.findViewById(R.id.Tbookmark);
        Upload = (Button)myView.findViewById(R.id.uploadprofile);

        authentication= FirebaseAuth.getInstance(); // get instance of my firebase console
        dbReference = FirebaseDatabase.getInstance().getReference(); // access to database
        firebaseUser = authentication.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.compose);
        if(fab!=null)fab.hide();


        TextView joindate = (TextView)myView.findViewById(R.id.MemberSincedate);
        TextView numPost = (TextView)myView.findViewById(R.id.NumberPosts);


        if(firebaseUser!=null){
            Bitmap b =((navigation)mActivity).getprofilepic();
            if(b!=null){
                final com.github.siyamed.shapeimageview.CircularImageView profileP = (com.github.siyamed.shapeimageview.CircularImageView) myView.findViewById(R.id.profileImage);
                profileP.setImageDrawable(new BitmapDrawable(mActivity.getResources(),b));
                profileP.setScaleType(ImageView.ScaleType.FIT_XY);

                Users users=((navigation)mActivity).getworkingUser();
                joindate.setText(users.getJoinDate());
                numPost.setText(Integer.toString(users.getNumOfPosts()));
            }
        }

        History.setOnClickListener(this);
        Bookmarks.setOnClickListener(this);

        History.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    History.setBackgroundResource(R.color.backgroundblue);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    History.setBackgroundResource(R.color.backgroundbluetouch);
                }
                return false;
            }
        });
        Bookmarks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    Bookmarks.setBackgroundResource(R.color.backgroundblue);
                }else if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    Bookmarks.setBackgroundResource(R.color.backgroundbluetouch);
                }
                return false;
            }
        });

        Upload.setOnClickListener(this);

        return myView;
    }



    public void ShowProgressDialog() { // progress
        if (pDialog == null) {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Loading Posts");
            pDialog.setIndeterminate(true);
        }
        pDialog.show();
    }
    public void HideProgressDialog() {
        if(pDialog!=null && pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
    public boolean checkReadExternalPermission(){
        String permission = "android.permission.READ_EXTERNAL_STORAGE"; // get permissions
        int res= mActivity.checkCallingOrSelfPermission(permission);
        return (res== PackageManager.PERMISSION_GRANTED);
    }
    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
    }
    public void onDestroy() {
        ViewGroup container = (ViewGroup)mActivity.findViewById(R.id.content_frame);
        container.removeAllViews();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    Toast.makeText(mActivity,"Permission needed to read image",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onClick(View v) {
        Handler handler = new Handler();
        switch (v.getId()){
            case R.id.Tposts:
                final ProfileTask getProfilePost = new ProfileTask("posts","Posts", myView,mActivity,dbReference,firebaseUser);
                getProfilePost.execute();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(getProfilePost.getStatus()==AsyncTask.Status.RUNNING){
                            getProfilePost.cancel(true);
                            HideProgressDialog();
                            Toast.makeText(mActivity,"Nothing was found",Toast.LENGTH_SHORT).show();
                        }
                    }
                },5000);
                break;
            case R.id.Tbookmark:
                final ProfileTask getProfilePost_bookmark = new ProfileTask("posts","Bookmarks", myView,mActivity,dbReference,firebaseUser);
                getProfilePost_bookmark.execute();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(getProfilePost_bookmark.getStatus()==AsyncTask.Status.RUNNING){
                            getProfilePost_bookmark.cancel(true);
                            HideProgressDialog();
                            Toast.makeText(mActivity,"Nothing was found",Toast.LENGTH_SHORT).show();
                        }
                    }
                },5000);
                break;
            case R.id.uploadprofile:
                if (checkReadExternalPermission()) {
                    final Intent galleryIntent = new Intent();
                    galleryIntent.setType("image/*");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    mActivity.startActivityForResult(galleryIntent, 2);
                }
                else{
                    requestForSpecificPermission();
                }
                break;

        }
    }
}
