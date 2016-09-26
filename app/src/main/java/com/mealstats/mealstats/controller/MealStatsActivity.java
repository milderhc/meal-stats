package com.mealstats.mealstats.controller;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mealstats.mealstats.R;
import com.mealstats.mealstats.controller.dummy.DummyMealInfo;
import com.mealstats.mealstats.services.GetNutritionalInfo;
import com.mealstats.mealstats.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class MealStatsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   FoodRetrievalFragment.OnListFragmentInteractionListener {

    private Uri pictureUri;
    private ImageView pictureImageView;
    private boolean loadFoodRetrievalFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //pictureImageView = (ImageView) findViewById(R.id.picture_image_view);
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
        loadFoodRetrievalFragment = true;
    }

    private void loadFragment ( Fragment newFragment ) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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

    private void analyzePicture() {
        GetNutritionalInfo infoService = new GetNutritionalInfo(this);
        String filePath = pictureUri.getPath();

        Log.d("img_debug", "Path " + filePath);

        try {
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
    }

    private void handleImageNotFoundError(FileNotFoundException e, String filePath){
        Log.d("img_debug", "File not found " + filePath);
    }

    private void handleVolleyError(VolleyError error){
        Log.d("deb_e", error.toString());
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
        Toast.makeText(this, item.mealName, Toast.LENGTH_LONG).show();
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

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_rate) {

        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
