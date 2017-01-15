package com.example.david.dpsproject.Fragments.Authentication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.R;
import com.example.david.dpsproject.navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by david on 2016-10-25.
 */
public class SignUp extends Fragment implements View.OnClickListener {
    View myView;

    EditText userName;
    EditText userPassword;
    EditText passwordConfirm;
    Button signUp;
    //Firebase variables
    FirebaseAuth authentication;
    DatabaseReference dbReference;
    Activity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    protected void createNewAccount(final String email, final String password){
        authentication.createUserWithEmailAndPassword(email,password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                ((navigation)mActivity).HideProgressDialog();
                if(!(task.isSuccessful())){
                    Toast.makeText(getActivity(),"authentication failed",Toast.LENGTH_SHORT).show();
                }
                else{

                    Date cDate = new Date();
                    String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

                    /*
                    default sub categories must be changed
                     */
                    ArrayList<String> sub = new ArrayList<String>();
                    sub.add("Jesus");
                    sub.add("Soccer");
                    sub.add("Uplifting");
                    Users u = new Users(email,password, new HashMap<String, ArrayList<String>>(),new HashMap<String, ArrayList<String>>(),
                            new HashMap<String, ArrayList<String>>(),"",sub,fDate,0, FirebaseInstanceId.getInstance().getToken());
                    dbReference.child("Users").child(task.getResult().getUser().getUid()).setValue(u);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame,new LogIn()).commit();

                }
            }
        });
    }
    public boolean checkpassword(){
        return (userPassword.getText().toString().equals(passwordConfirm.getText().toString()))?true:false;
    }

    public void onClick(View v) {
        if(v.getId()==R.id.signup) {
            if(checkpassword()) {
                createNewAccount(userName.getText().toString(), userPassword.getText().toString());
                ((navigation) mActivity).ShowProgressDialog();
            }else Toast.makeText(mActivity,"Password do not match",Toast.LENGTH_LONG).show();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Sign Up");
        myView = inflater.inflate(R.layout.sign_up,container,false);
        userName = (EditText)myView.findViewById(R.id.userName);
        userPassword = (EditText)myView.findViewById(R.id.userPassword);
        passwordConfirm =(EditText)myView.findViewById(R.id.passwordConfirm);
        signUp =(Button)myView.findViewById(R.id.signup);


        authentication= FirebaseAuth.getInstance(); // get instance of my firebase console
        dbReference = FirebaseDatabase.getInstance().getReference(); // access to database

        signUp.setOnClickListener(this);

        return myView;
    }
    public void onDestroy() {
        ViewGroup container = (ViewGroup)mActivity.findViewById(R.id.content_frame);
        container.removeAllViews();
        super.onDestroy();
    }

}
