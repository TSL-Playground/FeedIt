package edu.mit.urop.playground.tsl.feedit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScanResultActivity extends AppCompatActivity {

    public static final String SITUATION_ID_INTENT_KEY = "y";
    public static final String SITUATION_TEXT_INTENT_KEY = "x";

    int mSituationId;
    String mSituationText;
    TextView mTwSituationText;
    Button mBtnAddReaction, mBtnViewReactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        Intent fromScanActivity = getIntent();

        if(fromScanActivity != null){
            mSituationId = fromScanActivity.getExtras().getInt(SITUATION_ID_INTENT_KEY);
            mSituationText = fromScanActivity.getExtras().getString(SITUATION_TEXT_INTENT_KEY);
        }

        mTwSituationText = findViewById(R.id.tw_scan_situation_text);
        mBtnAddReaction = findViewById(R.id.btn_add_reaction);
        mBtnViewReactions = findViewById(R.id.btn_view_reactions);


        displaySituationInfo();
    }

    private void displaySituationInfo(){

        mTwSituationText.setText(mSituationText);


    }

    public void onAddReactionClicked(View view){

        Intent toAddReactionActivity = new Intent(this, AddReactionActivity.class);
        toAddReactionActivity.putExtra(AddReactionActivity.RECEIVE_SITUATION_ID_KEY, mSituationId);
        toAddReactionActivity.putExtra(AddReactionActivity.RECEIVE_SITUATION_TEXT_KEY, mSituationText);

        startActivity(toAddReactionActivity);
    }

    public void onViewReactionsClicked(View view){


    }



}
