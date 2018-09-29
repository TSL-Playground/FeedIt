package edu.mit.urop.playground.tsl.feedit.screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.screens.AddReactionActivity;


/**
 * This screen displays the content of the QR code that user has scanned in the ScanActivity(which triggered this activity.)
 * This activity also provides the user with the options of:
 * a) Adding a reaction to the situation in the QR Code.
 * b) View the previously given reactions to situation in the QR Code.
 */

public class ScanResultActivity extends AppCompatActivity {

    //Arbitrary constant for receiving the situation text extra from the intent from ScanActivity.
    public static final String SITUATION_ID_INTENT_KEY = "y";

    //Arbitrary constant for receiving the situation id extra from the intent from ScanActivity.
    public static final String SITUATION_TEXT_INTENT_KEY = "x";

    int mSituationId; //received situation id will be stored here.
    String mSituationText; //received situation text will be stored here.

    //UI widgets.
    TextView mTwSituationText;
    Button mBtnAddReaction, mBtnViewReactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        //Get a reference to the intent(from ScanActivity) that started this activity.
        Intent fromScanActivity = getIntent();

        //Receive the extras and set them to variables in the scope of ScanResultActivity(this one.)
        if(fromScanActivity != null){
            mSituationId = fromScanActivity.getExtras().getInt(SITUATION_ID_INTENT_KEY);
            mSituationText = fromScanActivity.getExtras().getString(SITUATION_TEXT_INTENT_KEY);
        }


        //Instantiating the UI widgets of the layout of this activity.
        mTwSituationText = findViewById(R.id.tw_scan_situation_text);
        mBtnAddReaction = findViewById(R.id.btn_add_reaction);
        mBtnViewReactions = findViewById(R.id.btn_view_reactions);

        //Helper method call to display the data received from the QR scan in ScanActivity.
        displaySituationInfo();
    }


    private void displaySituationInfo(){

        mTwSituationText.setText(mSituationText);


    }

    /**
     * Callback to the Add Reaction button taps.
     *
     * Passes the id and text of the situation to AddReactionActivity along with an intent.
     *
     * @param view Will be passed by Android automatically when the button is tapped.
     */
    public void onAddReactionClicked(View view){

        Intent toAddReactionActivity = new Intent(this, AddReactionActivity.class);
        toAddReactionActivity.putExtra(AddReactionActivity.RECEIVE_SITUATION_ID_KEY, mSituationId);
        toAddReactionActivity.putExtra(AddReactionActivity.RECEIVE_SITUATION_TEXT_KEY, mSituationText);

        startActivity(toAddReactionActivity);
    }

    /**
     * Callback to the View Reactions button taps.
     *
     * Passes the id of the situation to ViewReactionsActivity along with an intent.
     *
     * @param view Will be passed by Android automatically when the button is tapped.
     */
    public void onViewReactionsClicked(View view){

        Intent toViewReactionsActivity = new Intent(this, ViewReactionsActivity.class);
        toViewReactionsActivity.putExtra(ViewReactionsActivity.EXTRA_SITUATION_ID, mSituationId);

        startActivity(toViewReactionsActivity);

    }



}
