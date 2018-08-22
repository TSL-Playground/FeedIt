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

import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.models.Reaction;

public class AddReactionActivity extends AppCompatActivity{

    public static final String RECEIVE_SITUATION_TEXT_KEY = "19";
    public static final String RECEIVE_SITUATION_ID_KEY = "20";

    int mSituationId;
    String mSituationText;

    EditText mReactionTitleInput, mReactionDescriptionInput;
    Button mSubmitButton;

    DatabaseReference mDatabaseFeedIt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reaction);

        Intent fromScanResultActivity = getIntent();

        if(fromScanResultActivity != null){
            mSituationId = fromScanResultActivity.getIntExtra(RECEIVE_SITUATION_ID_KEY, 0 );
            mSituationText = fromScanResultActivity.getStringExtra(RECEIVE_SITUATION_TEXT_KEY);
        }

        //Initiate the views.

        mSubmitButton = findViewById(R.id.btn_submit_reaction);
        mReactionTitleInput = findViewById(R.id.et_reaction_title_input);
        mReactionDescriptionInput = findViewById(R.id.et_reaction_desc_input);

        //Get a reference to the FireBase database for the app.

        mDatabaseFeedIt = FirebaseDatabase.getInstance().getReference("reactions");
    }



    public void onSubmitReactionTapped(View view){


        String reactionTitle = mReactionTitleInput.getText().toString();
        String reactionDescription = mReactionDescriptionInput.getText().toString();

        String autogenReactionId = mDatabaseFeedIt.push().getKey(); // auto-generated unique id for the reaction.


        //Default values of number of likes and dislikes are 0. (last two arguments of the constructor.)
        Reaction reaction = new Reaction(mSituationId, reactionTitle, reactionDescription, 0, 0);

        mDatabaseFeedIt.child(autogenReactionId).setValue(reaction);

        Toast.makeText(getApplicationContext(), "Thank you!, your response is submitted.", Toast.LENGTH_SHORT).show();

        mReactionDescriptionInput.setText("");
        mReactionTitleInput.setText("");

        Intent toScanActivity = new Intent(this, ScanActivity.class);
        startActivity(toScanActivity);
    }

}
