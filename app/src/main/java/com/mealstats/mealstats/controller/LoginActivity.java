package com.mealstats.mealstats.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mealstats.mealstats.R;
import com.mealstats.mealstats.model.User;
import com.mealstats.mealstats.util.Constants;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ( !isLogged() ) {
            facebookLoginSetUp();
            //googleLoginSetUp();
        }
    }

    protected static User currentUser;
    private SharedPreferences sharedPreferences;

    private boolean isLogged () {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if ( sharedPreferences.getBoolean(Constants.IS_LOGGED,
                                          Constants.IS_LOGGED_DEFAULT_VALUE) ) {

            String userEmail = sharedPreferences.getString(Constants.EMAIL, null);

            //TODO get the real user
            currentUser = new User(userEmail);

            goToMainActivity();
            return true;
        }

        return false;
    }


    /*
    * Facebook Login
    * */

    private CallbackManager callbackManager;

    private void facebookLoginSetUp() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            try {
                                String email = object.getString(Constants.EMAIL);

                                logInUser(email);
                            }catch( Exception e){
                                Log.d("fb", "An error has ocurred logging in facebook");
                            }
                        });

                Bundle parameters = new Bundle();
                //parameters.putString("fields", "id,name,email");
                parameters.putString("fields", Constants.FACEBOOK_EMAIL);
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("fb", "An error has ocurred logging in facebook");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void logInUser (String userEmail) {
        //TODO check if user associated to userEmail is already registered

        currentUser = new User(userEmail);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.IS_LOGGED, true);
        editor.putString(Constants.EMAIL, userEmail);
        editor.commit();

        goToMainActivity();
    }


    public void goToMainActivity() {
        startActivity(new Intent(this, MealStatsActivity.class));
    }
}
