package edu.mit.urop.playground.tsl.feedit;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class ScanActivity extends AppCompatActivity {



    private IntentIntegrator qrScanner;
    public static final String RECEIVE_EXTRA_KEY = "x";
    TextView mGreetingTw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        mGreetingTw = findViewById(R.id.tw_user_greeting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent fromMainActivity = getIntent();

        if(fromMainActivity!= null && fromMainActivity.hasExtra(RECEIVE_EXTRA_KEY))
            mGreetingTw.setText("Hello, " + fromMainActivity.getStringExtra(RECEIVE_EXTRA_KEY));


        qrScanner = new IntentIntegrator(this);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



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

    public void scanButtonClicked(View view){
        qrScanner.initiateScan();
    }
}
