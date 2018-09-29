package edu.mit.urop.playground.tsl.feedit.screens;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.models.Reaction;

/**
 * This Activity manages the details screen that appears when the user taps on one of the reactions
 * from the list that is displayed in ViewReactionsActivity.
 *
 * In this screen the user can upvote/downvote a reaction and view the current status.
 * Can choose to write a review or read the reviews for the reaction.
 *
 *
 */
public class ReactionDetailsActivity extends AppCompatActivity{

    public static final String EXTRA_RECEPTION_KEY = "to the reaction details activity.";
    private static final String TAG = ReactionDetailsActivity.class.getSimpleName();

    //Declarin the UI Widgets.
    TextView twTitle, twDescription, twNumberOfLikes, twNumberOfDislikes;
    ImageButton btnLike, btnDislike;

    //Getting a reference to the tapped reaction and the updated version(in case upvote/downvote).
    Reaction passedReaction, updatedReaction;

    //Reference to the app's Firebase database backend.
    DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_details);

        //Get the intent that started this activity. (in this context, it is: ViewReactionsActivity)
        Intent starterIntent = getIntent();

        //If the intent is not null, fetch the data that's passed along with it.
        if(starterIntent != null ){

                passedReaction = Parcels.unwrap(starterIntent.getParcelableExtra(EXTRA_RECEPTION_KEY));
        }

        //Helper method call for instantiating the UI widgets. (Purpose: more concise and readable onCreate.)
        bindViews();

        //If ViewReactionsActivity actually passed a reaction here(upon user tap), display it's content.
        if(passedReaction != null){
            displayReactionData(passedReaction);
        }


    }

    //Inflate the UI widgets of this screen with the reaction that was tapped.
    private void displayReactionData(Reaction reaction){

        twTitle.setText(reaction.getmTitle().toString());
        twDescription.setText(reaction.getmDescription().toString());
        twNumberOfLikes.setText(String.valueOf(reaction.getmNumberLikes()));
        twNumberOfDislikes.setText(String.valueOf(reaction.getmNumberDislikes()));


    }

    //Simple helper method to instantiate the UI widgets and get a reference to the Firabase DB's "reactions" table.
    //Purpose is to pull all these method calls out of the onCreate method.
    private void bindViews(){

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("reactions");

        twTitle = findViewById(R.id.tw_reaction_detail_title);
        twDescription = findViewById(R.id.tw_reaction_detail_description);
        twNumberOfDislikes = findViewById(R.id.tw_num_dislikes);
        twNumberOfLikes = findViewById(R.id.tw_num_likes);
        btnDislike = findViewById(R.id.btn_detail_dislike);
        btnLike = findViewById(R.id.btn_detail_like);

    }


    /**Callback for the upvote button tap. Increments the mNumberLikes column for this reaction in the database.
     *
     * Important suggestion for future development: In this initial version a user can like or dislike the same reaction
     * countless times. THIS IS CURRENTLY THE MOST APPARENT BUG IN THIS APP. In the future, it would be optimal to track
     * if the user that is currently authenticated(in MainActivity) has liked or disliked this reaction so that they
     * can't repeatedly do it.
     *
     * @param view
     */
    public void onLikeButtonClicked(View view) {


        Toast.makeText(this, "LIKE BUTTON WAS TAPPED", Toast.LENGTH_SHORT).show();

        int currentRating  = passedReaction.getmNumberLikes();

        Log.d("FIRST", String.valueOf(currentRating));

        int newRating = currentRating + 1 ;

        Log.d("UPDATED", String.valueOf(newRating));

        //Create a new Reaction object with the updated values. 
        updatedReaction = new Reaction(
                passedReaction.getmReactionId(),
                passedReaction.getmSituationId(),
                passedReaction.getmTitle(),
                passedReaction.getmDescription(),
                newRating,
                passedReaction.getmNumberDislikes(), passedReaction.getmSituationText());

        //Update the database with the new number of upvotes.
        mDatabaseRef.child(passedReaction.getmReactionId()).child("mNumberLikes").setValue(updatedReaction.getmNumberLikes());

        /**Update the current screen with new number of votes.
         *
         * Suggestion: set a ValueEventListener to this field so that you make sure that the value in the database is in fact
         * the same as the value displayed on the screen (while the user is still in this screen, thus, before an update.)
         */
        twNumberOfLikes.setText(String.valueOf(updatedReaction.getmNumberLikes()));

        passedReaction = updatedReaction;
    }


    /**
     * Callback for the dislike button on the screen. PLease refer to the comments for the onLikeButtonCLicked() method
     * for obtaining the explanation for the workings of this method.
     *
     * Note: The suggestion in the onLikeButtonClicked method's comment applies to this method, as well.
     *
     * @param view
     */
    public void onDislikeButtonClicked(View view) {

        Toast.makeText(this, "DISLIKE BUTTON WAS TAPPED", Toast.LENGTH_SHORT).show();

        int currentRating  = passedReaction.getmNumberDislikes();

        Log.d("FIRST", String.valueOf(currentRating));

        int newRating = currentRating + 1 ;

        Log.d("UPDATED", String.valueOf(newRating));

        updatedReaction = new Reaction(
                passedReaction.getmReactionId(),
                passedReaction.getmSituationId(),
                passedReaction.getmTitle(),
                passedReaction.getmDescription(),
                passedReaction.getmNumberLikes(),
                newRating, passedReaction.getmSituationText());


        mDatabaseRef.child(passedReaction.getmReactionId()).child("mNumberDislikes").setValue(updatedReaction.getmNumberDislikes());

        twNumberOfDislikes.setText(String.valueOf(updatedReaction.getmNumberDislikes()));

        passedReaction = updatedReaction;

    }


    //Callback to the view reviews button. Passes the reaction to DisplayReviews activity.
    public void onReadReviewsClicked(View view){

        Intent toDisplayReviews = new Intent(this, DisplayReviewsActivity.class);

        Parcelable p = Parcels.wrap(passedReaction);
        Log.d(TAG, String.valueOf(p == null));

        toDisplayReviews.putExtra(DisplayReviewsActivity.EXTRA_RECEPTION_KEY, Parcels.wrap(passedReaction));
        startActivity(toDisplayReviews);
    }

    //Callback to the write review button. Passes the reaction to the WriteReviewActivity.
    public void onWriteReviewClicked(View view){

        Intent toWriteReviewActivity = new Intent(this, WriteReviewActivity.class);
        toWriteReviewActivity.putExtra(WriteReviewActivity.EXTRA_RECEPTION_KEY, Parcels.wrap(passedReaction));
        startActivity(toWriteReviewActivity);
    }


}
