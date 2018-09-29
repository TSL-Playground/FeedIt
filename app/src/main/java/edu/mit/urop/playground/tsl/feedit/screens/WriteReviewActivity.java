package edu.mit.urop.playground.tsl.feedit.screens;

import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.models.Reaction;
import edu.mit.urop.playground.tsl.feedit.models.Review;


/**
 * Feed-it allows the users to write reviews for reactions. This Activity manages that screen and handles that request.
 *
 */
public class WriteReviewActivity extends AppCompatActivity {

    //Log tag for debugging purposes.
    public static final String TAG = WriteReviewActivity.class.getSimpleName();

    //Arbitrary constant for receiving extras from other activities through intents.
    public static final String EXTRA_RECEPTION_KEY = "2LSJCNI59";

    //Declaring the UI widgets.
    EditText etReviewInput;
    Button btnSubmitReview;

    Reaction passedReaction; // reaction for which the user wants to write a review.

    //Reference to the Firebase database.
    DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        //Instantiate the UI widgets on the screen.
        etReviewInput = findViewById(R.id.et_review_input);
        btnSubmitReview = findViewById(R.id.btn_submit_review);

        //Get a reference to the Firabase database's reviews table.
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("reviews");

        //Get the intent that started this activity, currently this activity will only be started by the ReactionDetailsActivity.
        Intent fromReactionDetailsActivity = getIntent();

        //If the starting intent is not null, receive the data that is passed along with it.
        if(fromReactionDetailsActivity != null){

            passedReaction = Parcels.unwrap(fromReactionDetailsActivity.getParcelableExtra(EXTRA_RECEPTION_KEY));


        }

    }


    //Callback method for the submit button on the screen. Inserts the new review with the reaction id it's associated with.
    public void onSubmitReviewClicked(View view){

        //Get the user's input from the edittext widget.
        String reviewInput = etReviewInput.getText().toString();

        //Generate a unique id for the review that is given by the user.
        String reviewId = mDatabaseRef.push().getKey();

        //Instantiate a review object with the reviewid, reviewInout and the id of the reaction that the review is written for.
        Review review = new Review(passedReaction.getmReactionId(), reviewId, reviewInput );


        mDatabaseRef.child(reviewId).setValue(review);

        /**Notify the user that their review has been successfully submitted and jump back to the ReactionDetailsActivity
         * so the user can see the review they just posted.
         *
         * Note for the future developer: Current version presumes that this operation will succeed.
         * Please handle the case where it might fail and notify the user.
         */
        Toast.makeText(getApplicationContext(), "Your review has been submitted.", Toast.LENGTH_SHORT).show();

        Intent toReactionDetailsActivity = new Intent(this, ReactionDetailsActivity.class);

        //Since ReactionDetailsActivity may be started by multiple activities, have this activity identify itself in the intent.
        toReactionDetailsActivity.putExtra("source",  TAG);

        toReactionDetailsActivity.putExtra(ReactionDetailsActivity.EXTRA_RECEPTION_KEY, Parcels.wrap(passedReaction));

        startActivity(toReactionDetailsActivity);


    }
}
