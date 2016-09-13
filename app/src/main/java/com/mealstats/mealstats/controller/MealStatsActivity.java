package com.mealstats.mealstats.controller;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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

import com.mealstats.mealstats.R;
import com.mealstats.mealstats.services.GetNutritionalInfo;
import com.mealstats.mealstats.util.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MealStatsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Uri pictureUri;
    private ImageView pictureImageView;

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

        pictureImageView = (ImageView) findViewById(R.id.picture_image_view);
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

    private void selectImage () {
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
                    previewPicture();
                    processPicture();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            } if (requestCode == Constants.REQUEST_IMAGE_SELECT && data != null
                    && data.getData() != null) {
                //Preview and process image selected
                try {
                    pictureUri = data.getData();
                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), pictureUri);
                    File selectedPictureFile = getOutputMediaFile();
                    FileOutputStream fos = new FileOutputStream(selectedPictureFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
                    fos.close();
                    pictureUri = Uri.fromFile(selectedPictureFile);

                    previewPicture();
                    processPicture();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void previewPicture() throws NullPointerException {
        pictureImageView.setVisibility(View.VISIBLE);
        // bimatp factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 4;
        final Bitmap bitmap = BitmapFactory.decodeFile(pictureUri.getPath(), options);
        pictureImageView.setImageBitmap(bitmap);
    }

    private void processPicture() {
        GetNutritionalInfo infoService = new GetNutritionalInfo(this);
        String filePath = pictureUri.getPath();

        Log.d("img1", filePath);

        try {
            infoService.sendRequest(filePath,
                    (response -> Log.d("deb_r", response.toString())),
                    (error -> Log.d("deb_e", error.toString())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        File mediaStorageDir = new File(
                getExternalCacheDir(),
                Constants.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("img1", "Failed create "
                        + Constants.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            takePicture();
        } else if (id == R.id.nav_gallery) {
            selectImage();
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
