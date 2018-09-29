package edu.mit.urop.playground.tsl.feedit.screens;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import edu.mit.urop.playground.tsl.feedit.R;


/**
 *This is the launcher activity for the app.
 *Greets the user and offers the options of logging in using Facebook or Google APIs.
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{


    PackageInfo info;
    CallbackManager callbackManager;
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

    //Objects for the Google sign-in setup.
    SignInButton mGoogleSignInButton;
    GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CODE_GOOGLE_SIGN_IN = 10; //arbitrary constant.



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Facebook sign in setup.
        callbackManager = CallbackManager.Factory.create(); //initialize the callback manager which is the FB API client.
        registerCallbackForFacebook();



        //Google sign in setup.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()  // developer can add methods to ask additional information. (not recommended.)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        // Initialize the google sign in button and set an onclick listener to make it reactive.
        mGoogleSignInButton = findViewById(R.id.google_button);
        mGoogleSignInButton.setOnClickListener(this);


    }


    //Register the callback for the Facebook sign in button.
    private void registerCallbackForFacebook(){
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent toScanActivity = new Intent(MainActivity.this, ScanActivity.class);
                        startActivity(toScanActivity);
                    }

                    //Called if the user interrupts the facebook login by some action.
                    @Override
                    public void onCancel() {

                        Toast.makeText(MainActivity.this, "Login Cancelled", Toast.LENGTH_SHORT).show();

                    }

                    //Called if there is a server error on the Facebook's side.
                    @Override
                    public void onError(FacebookException exception) {

                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //Methods for operations regarding Google.

    private void startGoogleSignInIntent(){

        Log.d("TAG", "Called");

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN);
    }


    /**
     * Helper function called after the user attempts to sign-in with Google.
     * If successful, greet the user and take them to scan screen. Else, notify of login failure.
     *
     * @param : Result code that will be passed by the OnActivityResult method. (read code below.)
     */


    private void handleGoogleSignInResult(GoogleSignInResult result){

        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();

            Toast.makeText(this, "Hello, "+ account.getDisplayName(),Toast.LENGTH_SHORT).show();

            Intent toScanActivity = new Intent(MainActivity.this, ScanActivity.class);
            toScanActivity.putExtra(ScanActivity.RECEIVE_EXTRA_KEY,account.getGivenName()+ "!");
            startActivity(toScanActivity);




        }else{
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method will be called by default in onDestroy() and will sign the user out.
     *
     * Note: If a future developer wants to change this default behavior of the app and chooses to let the
     * user remain logged in, they should just delete the call to this method in onDestroy().
     *
     */
    private void googleSignOut(){

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Initialize the action bar menu.
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;

    }


    /**
     * Defines the action to be taken when the user taps on an option from the drop-down menu.
     * Options are start the scan activity, and sign out.
     *
     *  A future developer can choose to add to/remove from options to this menu. They should write their method
     *  and include the call of those methods in onOptionsItemSelected().
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_to_scan){
            Intent toScan = new Intent(this, ScanActivity.class);
            startActivity(toScan);
            return true;
        }
        else if(item.getItemId() == R.id.action_google_sign_out){
            googleSignOut();
        }

        return super.onOptionsItemSelected(item);

    }


    //If connection to Google services fail.
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Toast.makeText(MainActivity.this, "Connection failed, Google service currently unavailable.", Toast.LENGTH_SHORT).show();
    }

    //Sign the user out if they kill the app process on their phone (current default behavior of the app).
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(isLoggedIn)
            LoginManager.getInstance().logOut();

        googleSignOut();
    }


    //Callback for the result from the Facebook and Google sign in.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GOOGLE_SIGN_IN){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }

        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onClick(View v) {

        // If the Google Sign In Button is clicked.
        if(v.getId() == R.id.google_button){
            startGoogleSignInIntent();
        }

        //Facebook sign-in button has its onCLickListener set up in onCreate(). Therefore, it is missing in this method.
    }
}

