package edu.mit.urop.playground.tsl.feedit.screens;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import edu.mit.urop.playground.tsl.feedit.R;


/**
 * Scan activity is the first screen after the user sign-in. This activity starts and processes the QR Code scanning.
 *
 * For the actual barcode scanning, Zxing open-source library is used. (Check build.gradle (Module:app) file to alter dependency.)
 *
 */
public class ScanActivity extends AppCompatActivity {



    private IntentIntegrator qrScanner; //Client object for the Zxing API.
    public static final String RECEIVE_EXTRA_KEY = "x"; // arbitrary constant.
    TextView mGreetingTw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mGreetingTw = findViewById(R.id.tw_user_greeting); // initialize the user greeting textview.

        //Setting up the action bar which will provide the user with an in-app back button.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //This activity will be started by the MainActivity.
        Intent fromMainActivity = getIntent();

        /**
         *If the intent that started this activity has an "extra" that is sent to this activity,
         * receive it with the receive key of this activity and set it to the textview.(message will be the user's name.)

         **/
        if(fromMainActivity!= null && fromMainActivity.hasExtra(RECEIVE_EXTRA_KEY))
            mGreetingTw.setText("Hello, " + fromMainActivity.getStringExtra(RECEIVE_EXTRA_KEY));


        //Instantiate the client for the Zxing API client.
        qrScanner = new IntentIntegrator(this);

    }

    //Callback for the action bar back button.
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    /**
     * The scanning operation is passed to a client Zxing application.
     * If the user's device does not currently have it installed,they will be prompted for permission to install this app.
     * After installation, Feed-it will automatically start that app, and allow the user to scan the QR code.
     *
     * Format of the information that's encoded in the QR codes is JSON. This method processes the JSON and passes the info
     * as extras to the ScanResultActivity.
     *
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews

                    //Will be passed to the Post-Scan Activity.
                    Bundle situationInfo = new Bundle();
                    situationInfo.putInt(ScanResultActivity.SITUATION_ID_INTENT_KEY, Integer.valueOf(obj.getString("id")));
                    situationInfo.putString(ScanResultActivity.SITUATION_TEXT_INTENT_KEY,obj.getString("text"));

                    Intent toScanResultActivity = new Intent(this, ScanResultActivity.class);
                    toScanResultActivity.putExtras(situationInfo);
                    startActivity(toScanResultActivity);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the QR Code.
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    //Callback for the circle scan button displayed on the screen.
    public void scanButtonClicked(View view){
        qrScanner.initiateScan();
    }
}
