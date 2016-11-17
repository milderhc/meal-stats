package com.mealstats.mealstats.controller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mealstats.mealstats.R;
import com.mealstats.mealstats.controller.dummy.DummyMealInfo;
import com.mealstats.mealstats.controller.foodList.FoodRetrievalFragment;
import com.mealstats.mealstats.controller.statsList.StatsFragment;
import com.mealstats.mealstats.services.GetNutritionalInfo;
import com.mealstats.mealstats.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class MealStatsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   FoodRetrievalFragment.OnListFragmentInteractionListener {

    private static final int TIP_FREQUENCY = 4;
    private Uri pictureUri;
    private ImageView pictureImageView;
    private ProgressDialog onRequestBackendDialog;
    private int requestsCounter;

    private String[] TIPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle(getResources().getString(R.string.title_activity_meal_stats));

        setContentView(R.layout.activity_meal_stats);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setUpNavigationView();
        showInstructionsMessage();
        //pictureImageView = (ImageView) findViewById(R.id.picture_image_view);
        requestsCounter = 0;

        TIPS  = getResources().getStringArray(R.array.tips);
    }

    private void setUpNavigationView () {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        TextView headerEmailTextView = (TextView) headerView.findViewById(R.id.header_email);
        headerEmailTextView.setText(LoginActivity.currentUser.getEmail());
    }

    private void showInstructionsMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Instructions");
        builder.setMessage(getResources().getString(R.string.take_picture_message));
        builder.setPositiveButton("Got it",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });
        builder.show();
    }

    private boolean deviceSupportCamera() {
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pictureUri = getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        startActivityForResult(intent, Constants.REQUEST_IMAGE_CAPTURE);
    }

    private void selectPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                Constants.REQUEST_IMAGE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {
                //Preview and process image taken
                try {
                    processPicture(BitmapFactory.decodeFile(pictureUri.getPath()));
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            } if (requestCode == Constants.REQUEST_IMAGE_SELECT && data != null
                    && data.getData() != null) {
                //Preview and process image selected
                try {
                    pictureUri = data.getData();
                    processPicture(MediaStore.Images.Media.getBitmap(getContentResolver(),
                            pictureUri));
                } catch ( IOException e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processPicture ( Bitmap bitmap ) throws IOException {
        compressPicture(bitmap, Constants.COMPRESS_QUALITY);
        //previewPicture();
        analyzePicture();
    }

    private void loadFragment ( Fragment newFragment ) {
        onRequestBackendDialog.hide();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        if(requestsCounter % TIP_FREQUENCY == 0){
            showTip();
        }
        requestsCounter++;
    }

    @SuppressLint("NewApi")
    private void showTip(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tip");
        builder.setMessage(TIPS[ThreadLocalRandom.current().nextInt(TIPS.length)]);
        builder.setPositiveButton("Got it",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });
        builder.show();
    }

    private void previewPicture() throws NullPointerException {
        pictureImageView.setVisibility(View.VISIBLE);
        Bitmap bitmap = BitmapFactory.decodeFile(pictureUri.getPath());
        pictureImageView.setImageBitmap(bitmap);
    }

    private void compressPicture (Bitmap bitmap, int quality) throws IOException {
        File selectedPictureFile = getOutputMediaFile();
        FileOutputStream fileOutputStream = new FileOutputStream(selectedPictureFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);
        fileOutputStream.close();
        pictureUri = Uri.fromFile(selectedPictureFile);
    }

    /**
     * Creates file uri to store image
     */
    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /*
     * Returns saved image
     */
    private File getOutputMediaFile() {
        File mediaStorageDir = new File(getExternalCacheDir(), Constants.PICTURE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists())
            if (!mediaStorageDir.mkdirs()) {
                Log.d("img_debug", "Failed creating "
                        + Constants.PICTURE_DIRECTORY_NAME + " directory");
                return null;
            }

        return new File(mediaStorageDir.getPath() + File.separator +
                        Constants.PICTURE_SELECTED_NAME);
    }

    private void initLoadingDialog(){
        onRequestBackendDialog = new ProgressDialog(this);
        onRequestBackendDialog.setTitle(getResources().getString(R.string.loadingBackendTitle));
        onRequestBackendDialog.setMessage(getResources().getString(R.string.loadingBackendMessage));
    }

    private void analyzePicture() {
        GetNutritionalInfo infoService = new GetNutritionalInfo(this);
        String filePath = pictureUri.getPath();

        Log.d("img_debug", "Path " + filePath);

        try {
            initLoadingDialog();
            onRequestBackendDialog.show();
            infoService.sendRequest(filePath,
                    (response -> loadFragment(FoodRetrievalFragment.newInstance(response))), //Remeber to handle errors appropiatley as are
                    (errorResponse -> handleBackendError(errorResponse)), //defined in the backend.
                    (error -> handleVolleyError(error)));      //Any possible volley error
        } catch (FileNotFoundException e) {
            handleImageNotFoundError(e, filePath);
            //e.printrivStackTrace();
        }
    }

    private void handleBackendError(Map<String, String> errorResponse){
        Log.d("backend_error", errorResponse.get("error"));
        Log.d("backend_error", errorResponse.get("details"));
        if(onRequestBackendDialog != null)
            onRequestBackendDialog.hide();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(errorResponse.get("error"));
        builder.setPositiveButton("Got it",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });
        builder.show();
    }

    private void handleImageNotFoundError(FileNotFoundException e, String filePath){
        if(onRequestBackendDialog != null)
            onRequestBackendDialog.hide();
        Log.d("img_debug", "File not found " + filePath);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(getResources().getString(R.string.image_not_found_error));
        builder.setPositiveButton("Got it",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });
        builder.show();
    }

    private void handleVolleyError(VolleyError error){
        if(onRequestBackendDialog != null)
            onRequestBackendDialog.hide();
        Log.d("deb_e", error.toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(getResources().getString(R.string.network_error));
        builder.setPositiveButton("Got it",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {}
                });
        builder.show();
    }

    @Override
    public void onResume () {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.PICTURE_URI, pictureUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pictureUri = savedInstanceState.getParcelable(Constants.PICTURE_URI);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meal_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(DummyMealInfo item) {
        loadFragment(StatsFragment.newInstance(item.stats));
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            takePicture();
        } else if (id == R.id.nav_gallery) {
            selectPicture();
        } else if (id == R.id.nav_settings) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_content, ConfigFragment.newInstance("",""));
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_rate) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.logout_title);
            builder.setMessage(R.string.logout_message);
            builder.setPositiveButton(R.string.yes_message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(MealStatsActivity.this);
                    SharedPreferences.Editor ed = mPrefs.edit();
                    ed.clear();
                    ed.commit();
                    try {
                        Runtime runtime = Runtime.getRuntime();
                        runtime.exec("pm clear com.mealstats.mealstats");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            builder.setNegativeButton(R.string.no_message, null);
            builder.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
