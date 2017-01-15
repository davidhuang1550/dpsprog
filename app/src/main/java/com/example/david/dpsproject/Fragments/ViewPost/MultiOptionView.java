package com.example.david.dpsproject.Fragments.ViewPost;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Notifications.SendNotification;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by david on 2017-01-05.
 */

public class MultiOptionView extends Fragment implements View.OnClickListener {
    private Activity mActivity;
    private View myView;
    private Post post;
    private int responseNum;
    HashMap<String, Integer> options;
    private TableLayout tableLayout;
    private HashMap<Integer,TextView> OptionView;
    private static int[] optionId= {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
    private int previously_selected;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.multi_answer_view, container, false);
        responseNum=0;
        previously_selected=0;
        OptionView = new HashMap<>();
        tableLayout= (TableLayout)myView.findViewById(R.id.layouttable);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = firebaseAuth.getCurrentUser();


        Button button= (Button)myView.findViewById(R.id.submit_answer);
        button.setOnClickListener(this);
        Bundle bundle = getArguments();
        post = (Post)bundle.getSerializable("Post_Object");
        if(post!=null){
            init();
        }
        return myView;

    }

    public void init(){
        options = post.getPiechart();
        Iterator it= options.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            tableLayout.addView(addRow(pair),responseNum);
            responseNum++;
        }
    }
    public TableRow addRow(Map.Entry pair){
        TableRow tr = new TableRow(mActivity);
        TextView textView = new TextView(mActivity);

        textView.setId(optionId[responseNum]);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(50,10,50,10);
        textView.setBackgroundResource(R.drawable.loginbutton);
        textView.setGravity(Gravity.CENTER);
        textView.setText(pair.getKey().toString());
        textView.setOnClickListener(this);



        tr.addView(textView,params);
        OptionView.put(optionId[responseNum],textView);
        return tr;
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.submit_answer){
            pushChanges(OptionView.get(previously_selected).getText().toString());
        }
        else{
            if(previously_selected==0){
                previously_selected=view.getId();
                setClicked(OptionView.get(view.getId()));
            }else{
                setUnClicked(OptionView.get(previously_selected));
                previously_selected=view.getId();
                setClicked(OptionView.get(view.getId()));
            }
        }
    }
    public void setClicked(TextView textView){
        textView.setBackgroundResource(R.drawable.onselectbutton);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(25,5,25,5);
        textView.setLayoutParams(params);
    }
    public void setUnClicked(TextView textView){
        textView.setBackgroundResource(R.drawable.loginbutton);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(50,10,50,10);
        textView.setLayoutParams(params);
    }
    public void pushChanges(String s){
        options.put(s,options.get(s)+1);
        post.incTotalPost();
        post.setPiechart(options);
        try {
            databaseReference.child("Sub").child(post.getSubN()).child("posts").child(post.getKey()).setValue(post);
            Users users = ((navigation) mActivity).getworkingUser();
            ArrayList<String> templist= users.AddToViewed(post.getSubN(), post.getKey());
            databaseReference.child("Users").child(firebaseUser.getUid()).child("Viewed").child(post.getSubN()).setValue(templist);
        }catch(DatabaseException e){
            e.printStackTrace();
        }
        PieChartFragment pieChartFragment = new PieChartFragment();
        sendNotification();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Post_Object",post);
        pieChartFragment.setArguments(bundle);
        FragmentManager fragmentManager = mActivity.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.replaceable_frame, pieChartFragment).commit();
    }
    public void sendNotification(){
        SendNotification sendNotification = new SendNotification(mActivity,post.getPosterId(),post.getKey(),post.getSubN());
        sendNotification.send();
    }

}
