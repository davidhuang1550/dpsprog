package com.example.david.dpsproject;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.app.FragmentManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.dpsproject.AsyncTask.UserAsyncTask;
import com.example.david.dpsproject.Class.Post;
import com.example.david.dpsproject.Class.Users;
import com.example.david.dpsproject.Dialog.OptionsDialog;
import com.example.david.dpsproject.Fragments.FrontPage;
import com.example.david.dpsproject.Fragments.Authentication.LogIn;
import com.example.david.dpsproject.Fragments.ProfileFragment;
import com.example.david.dpsproject.Fragments.ViewPost.postview;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.DataBaseConnectionsPresenter;
import com.example.david.dpsproject.Presenter.UsedByMoreThanOneClass.FabPresenter;
import com.example.david.dpsproject.Presenter.SortByPresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
public class navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,FragmentManager.OnBackStackChangedListener{
    protected Toolbar toolbar;
    private NavigationView navigationView;

    private FabPresenter fabPresenter;
    private DataBaseConnectionsPresenter dataBaseConnectionsPresenter;
    private FrontPage frontPage;
    private String filePath;
    private  Bitmap decodedprofilepic;
    private  ProgressDialog pDialog;

    private Menu subMenu;
    private MenuItem subscribeId;
    private MenuItem unsubscribId;
    private MenuItem SortBy;
    private Menu nav_Menu;
    private  Bundle bundle;

    private  View CategoryView;
    private  Users tempU;
    private String SubCat;
    private SortByPresenter sortByPresenter;
    private boolean setFreshToken;


    private Uri imageUpload;
    private ListView  listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setShowHideAnimationEnabled(true);
        setFreshToken=false;
        dataBaseConnectionsPresenter = new DataBaseConnectionsPresenter();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.compose);
        FloatingActionButton  fab_image = (FloatingActionButton) findViewById(R.id.compse_images);
        FloatingActionButton fab_desc = (FloatingActionButton) findViewById(R.id.compse_desc);
        fab_desc.setSize(FloatingActionButton.SIZE_MINI);
        fab_image.setSize(FloatingActionButton.SIZE_MINI);

        fabPresenter= new FabPresenter(fab,fab_image,fab_desc,dataBaseConnectionsPresenter,this);

        imageUpload =null;

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        subMenu=navigationView.getMenu();
        nav_Menu = navigationView.getMenu();
        FragmentManager fragmentManager = getFragmentManager();
        frontPage= new FrontPage();
        if(dataBaseConnectionsPresenter.getFirebaseUser()!=null){
            getUser();
        }
        else{
            fragmentManager.beginTransaction().add(R.id.content_frame,frontPage,"FrontPage").commit();
        }





    }
    public boolean isNotification(){
        Intent temp=getIntent();
        bundle= temp.getExtras();
        if(bundle!=null){
            if(bundle.getBundle("Info")!=null)return true;
        }
        return false;
    }
    public void setPostview(){
        final FragmentManager fragmentManager = getFragmentManager();
        dataBaseConnectionsPresenter.getDbReference().child("Sub").child(bundle.getBundle("Info").getString("SubId"," ")).child("posts").
                child(bundle.getBundle("Info").getString("PostId"," ")).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Bundle bundle1 = new Bundle();

                Post post = dataSnapshot.getValue(Post.class);
                if(post!=null) {
                    bundle1.putSerializable("Post_Object", (Serializable) post);

                    postview pview = new postview();
                    pview.setArguments(bundle1);
                    fragmentManager.beginTransaction().add(R.id.content_frame, pview).commit();
                }
                else{
                    //display toast
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void hideStatusBar(){
      //  toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
        getSupportActionBar().hide();
        //toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();

    }
    public void showStatusBar(){
     //   toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();

        getSupportActionBar().show();
       // toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
    }
    public DataBaseConnectionsPresenter getDataBaseConnectionsPresenter(){
        return dataBaseConnectionsPresenter;
    }
    public void setprofilepic(Bitmap p){
        decodedprofilepic=p;
    }
    public Bitmap getprofilepic(){
        return decodedprofilepic;
    }
    public Toolbar getToolbar(){
        return toolbar;
    }

    public void setSubCat(String sub){
        SubCat=sub;
    }
    public boolean isSetFreshToken() {
        return setFreshToken;
    }
    public void refreshToken(){
        if(dataBaseConnectionsPresenter.getFirebaseUser()!=null)dataBaseConnectionsPresenter.getDbReference().child("Users").child(dataBaseConnectionsPresenter.getUID())
                .child("FcmToken").setValue(FirebaseInstanceId.getInstance().getToken());
    }
    public void setSetFreshToken(boolean setFreshToken) {
        this.setFreshToken = setFreshToken;
    }

    public void setPostView(ListView l){listView=l;}

    @Override
    public void onBackStackChanged() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (getFragmentManager().getBackStackEntryCount() > 0 ){

            getFragmentManager().popBackStack();

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showFab();
    }
    public void showFab(){

        fabPresenter.showFab();
    }
    public void hideFab(){
        fabPresenter.hideFab();
    }
    public void HideSort(){
        SortBy.setVisible(false);
    }
    public void ShowSort(){
        SortBy.setVisible(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        subscribeId=menu.getItem(0);
        subscribeId.setVisible(false);
        unsubscribId=menu.getItem(1);
        unsubscribId.setVisible(false);
        SortBy=menu.getItem(2);
        SortBy.setVisible(false);
        return true;
    }
    public void showUnsubscribe(){
        if(unsubscribId!=null)unsubscribId.setVisible(true);
    }
    public void showSubscribe(){
        if(subscribeId!=null)subscribeId.setVisible(true);
    }
    public void hideUnsubscribe(){
        if(unsubscribId!=null)unsubscribId.setVisible(false);
    }
    public void hidesubscribe(){
        if(subscribeId!=null)subscribeId.setVisible(false);
    }
    public void hideAllSubscribe(){

        if(subscribeId!=null)subscribeId.setVisible(false);
        if(unsubscribId!=null)unsubscribId.setVisible(false);

    }

    private void pushsubtodatabase(){
        dataBaseConnectionsPresenter.getDbReference().child("Users").child(dataBaseConnectionsPresenter.getUID()).child("Subcategory").setValue(tempU.getSubcategory());
    }
    public void reload_menu(){
        Menu menu=getSubMenu();
        if(menu!=null){
            menu.removeGroup(R.id.second_nav);
            ArrayList<String> subcat =tempU.getSubcategory();
            for(int i=0; i<subcat.size();i++){
                menu.add(R.id.second_nav,Menu.NONE,0,subcat.get(i));
            }
        }
    }
    public void remove_menu(){
        Menu menu=getSubMenu();
        if(menu!=null){
            menu.removeGroup(R.id.second_nav);
        }
    }
    public void ModifySub(String s,int p){
        if(tempU!=null){
            if(p==0){
                tempU.addSub(s);
                pushsubtodatabase();
            }else if(p==1){
                tempU.deleteSub(s);
                pushsubtodatabase();
            }else{Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();}

        }else{Toast.makeText(this,"Please sign in to subscribe",Toast.LENGTH_SHORT).show();}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(SubCat!=null) {
            if (id == R.id.subscribe) {
                ModifySub(SubCat,0);
                reload_menu();
                showUnsubscribe();
                hidesubscribe();

            } else if (id == R.id.Unsubscribe) {
                reload_menu();
                ModifySub(SubCat,1);
                showSubscribe();
                hideUnsubscribe();
            }
            else{
                listView = (ListView)findViewById(R.id.postview);
                sortByPresenter = new SortByPresenter(dataBaseConnectionsPresenter,SubCat,this,listView,CategoryView);
                if(id==R.id.SortByYes){
                    sortByPresenter.SortByYes();
                    sortByPresenter.sort();
                } else if(id==R.id.SortByNo){
                    sortByPresenter.SortByNo();
                    sortByPresenter.sort();
                } else if(id==R.id.SortByResponses){
                    sortByPresenter.SortByResponse();
                    sortByPresenter.sort();
                }

            }
        }

        return super.onOptionsItemSelected(item);
    }
    public void remove_nav_image(){
        View Layout = (View)findViewById(R.id.navPic);
        Layout.setBackgroundResource(R.drawable.default_desktop);
    }
    public void freeUserData(){
        hideFab();
        setLogintrue();
        TextView name = (TextView) findViewById(R.id.headText); // remove menu name
        name.setText("");
        remove_menu();
        remove_nav_image();
        tempU=null;

    }
    public void setLogintrue(){
        nav_Menu.findItem(R.id.login).setVisible(true);// set logout and login respectively
        nav_Menu.findItem(R.id.signout).setVisible(false);
        nav_Menu.findItem(R.id.profile).setVisible(false);
    }
    public void setLoginFalse(){
        nav_Menu.findItem(R.id.login).setVisible(false);
        nav_Menu.findItem(R.id.profile).setVisible(true);
        nav_Menu.findItem(R.id.signout).setVisible(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        HideProgressDialog();
    }

    public void setCategoryView(View categoryView) {
        CategoryView = categoryView;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id=item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();


        if (id == R.id.frontpage) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new FrontPage(),"FrontPage").commit();
        }
        else if(id==R.id.profile){
            fragmentManager.beginTransaction().replace(R.id.content_frame,new ProfileFragment()).commit();
        }else if (id == R.id.login) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new LogIn()).commit();
        } else if(id == R.id.search){
           // fragmentManager.beginTransaction().add(R.id.content_frame,new SearchFragment(),"search").commit();
            OptionsDialog optionsDialog = new OptionsDialog();
            optionsDialog.show(getFragmentManager(),"options Dialog");

        } else if(id==R.id.signout){
            freeUserData();
            FirebaseAuth.getInstance().signOut();
            fragmentManager.beginTransaction().replace(R.id.content_frame,new LogIn()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFirebaseUser(FirebaseUser firebaseUsers){
        dataBaseConnectionsPresenter.setFirebaseUser(firebaseUsers);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    Toast.makeText(this,"Permission needed to read image",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public Users getworkingUser(){
        return tempU;
    }
    public void getUser(){

        final UserAsyncTask getuserName = new UserAsyncTask(this, dataBaseConnectionsPresenter.getDbReference(), dataBaseConnectionsPresenter.getFirebaseUser(),frontPage);
        Handler userhandler = new Handler();

        getuserName.execute();

        Runnable userthread= new Runnable() {
            @Override
            public void run() {
                if(getuserName.getStatus()==AsyncTask.Status.RUNNING){
                    getuserName.cancel(true);
                    HideProgressDialog();
                    Toast.makeText(getApplicationContext(),"Error has occured ",Toast.LENGTH_SHORT).show();
                }
            }
        };
        userhandler.postDelayed(userthread,5000);
    }
    public void setUser(Users user){
        tempU=user;
    }

    public boolean checkReadExternalPermission(){
        String permission = "android.permission.READ_EXTERNAL_STORAGE"; // get permissions
        int res= this.checkCallingOrSelfPermission(permission);
        return (res== PackageManager.PERMISSION_GRANTED);
    }
    public void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
    }

    public Menu getSubMenu(){
        return subMenu;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==1){
                imageUpload = data.getData();
                filePath=getPath(this,imageUpload);

                final String action = data.getDataString();
                String prefix = "/image";
                String split = action.substring(action.indexOf(prefix)+prefix.length());
                TextView textView = (TextView)findViewById(R.id.uploadTextview);
                textView.setText("image"+split);
            }
            else if(requestCode==2){
                imageUpload = data.getData();
                filePath=getPath(this,imageUpload);

                //if (checkReadExternalPermission()) {
                    final BitmapFactory.Options options = new BitmapFactory.Options();

                    options.inSampleSize = 3;
                    options.inScaled=false;
                    Bitmap bitMap = BitmapFactory.decodeFile(filePath, options);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] bytes = stream.toByteArray();

                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://dpsproject-85e85.appspot.com/Images/");
                    DatabaseReference dbImageRef= dataBaseConnectionsPresenter.getDbReference().child("Image").push();
                    dbImageRef.setValue(new String("0"));
                    StorageReference ImageRef = storageRef.child(dbImageRef.getKey());
                    UploadTask uploadTask = ImageRef.putBytes(bytes);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
                tempU.setPicture(dbImageRef.getKey());
                dataBaseConnectionsPresenter.getDbReference().child("Users").child(dataBaseConnectionsPresenter.getUID()).child("picture").setValue(dbImageRef.getKey());
            }


        }
        else{
            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
    }

    public void ShowProgressDialog() { // progress
        if (pDialog == null) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading Posts");
            pDialog.setIndeterminate(true);
        }
        pDialog.show();
    }
    public void HideProgressDialog() {
        if(pDialog!=null && pDialog.isShowing()){
            pDialog.dismiss();
            pDialog.cancel();
        }
    }
    public String getFilePath(){return filePath;}
    public Uri getImageUpload(){return imageUpload;}
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                   // contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                   // contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}

