package edu.mit.urop.playground.tsl.feedit.screens;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.models.Reaction;

/**
 * This activity allows the user to add a new reaction to the currently scanned situation.
 * This activity is triggered by the ScanResultActivity.
 *
 * This activity interacts with the Firebase database(inserts data into it) throught the DatabaseReference client object.
 */

public class AddReactionActivity extends AppCompatActivity{

    //Arbitrary constants for receiving extras from the ScanResult activity.
    public static final String RECEIVE_SITUATION_TEXT_KEY = "19";
    public static final String RECEIVE_SITUATION_ID_KEY = "20";


    //ID and Text of the situation that was passed here by the ScanResult activity.
    int mSituationId;
    String mSituationText;

    //Declaring the UI widgets.
    EditText mReactionTitleInput, mReactionDescriptionInput;
    Button mSubmitButton;

    DatabaseReference mDatabaseFeedIt; //Client object for the Firebase realtime database client.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reaction);

        Intent fromScanResultActivity = getIntent(); // get a reference to the intent that triggered this activity.

        //If the intent is not null, receive the data that's passed along with it and store it locally within this activity.
        if(fromScanResultActivity != null){
            mSituationId = fromScanResultActivity.getIntExtra(RECEIVE_SITUATION_ID_KEY, 0 );
            mSituationText = fromScanResultActivity.getStringExtra(RECEIVE_SITUATION_TEXT_KEY);
        }

        //Instantiate the UI widgets.

        mSubmitButton = findViewById(R.id.btn_submit_reaction);
        mReactionTitleInput = findViewById(R.id.et_reaction_title_input);
        mReactionDescriptionInput = findViewById(R.id.et_reaction_desc_input);

        //Get a reference to the FireBase Realtime Database API.
        mDatabaseFeedIt = FirebaseDatabase.getInstance().getReference("reactions");
    }


    /**
     * Callback method to Submit button.
     *
     * Insert the reaction title and the reaction description to the Firebase database.
     *
     * @param view
     */
    public void onSubmitReactionTapped(View view){

        //Get the title and the description from the UI edittext.
        String reactionTitle = mReactionTitleInput.getText().toString();
        String reactionDescription = mReactionDescriptionInput.getText().toString();

        //Have the Firebase DB generate a unique key for the new row of data.
        String autogenReactionId = mDatabaseFeedIt.push().getKey(); // auto-generated unique id for the reaction.

        //Default values of number of likes and dislikes are 0. (last two arguments of the constructor.)
        Reaction reaction = new Reaction(autogenReactionId, mSituationId,
                reactionTitle, reactionDescription, 0, 0, mSituationText);

        //Insert the data into the row whose unique key was created in line 88.
        mDatabaseFeedIt.child(autogenReactionId).setValue(reaction);

        //Notify the user that the insertion was successful.
        Toast.makeText(getApplicationContext(), "Thank you!, your response is submitted.", Toast.LENGTH_SHORT).show();

        //Clear the UI input edittext fields.
        mReactionDescriptionInput.setText("");
        mReactionTitleInput.setText("");

        /**
         * Note developers who may work on this app in the future:
         *
         * Current default behavior of the app is to take the user back to the scan activity(for them to scan another card.)
         * You may want to change this behavior(or maybe ask the user what they want to do next.)
         *
         * Please change the code in the two lines below to implement your own flow of user experience.
         *
         */
        Intent toScanActivity = new Intent(this, ScanActivity.class);
        startActivity(toScanActivity);
    }

}
