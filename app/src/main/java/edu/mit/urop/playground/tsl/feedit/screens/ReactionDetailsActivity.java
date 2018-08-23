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

public class ReactionDetailsActivity extends AppCompatActivity{

    public static final String EXTRA_RECEPTION_KEY = "to the reaction details activity.";
    private static final String TAG = ReactionDetailsActivity.class.getSimpleName();

    TextView twTitle, twDescription, twNumberOfLikes, twNumberOfDislikes;
    ImageButton btnLike, btnDislike;
    Reaction passedReaction, updatedReaction;
    DatabaseReference mDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reaction_details);


        Intent starterIntent = getIntent();

        if(starterIntent != null ){

                passedReaction = Parcels.unwrap(starterIntent.getParcelableExtra(EXTRA_RECEPTION_KEY));
        }

        bindViews();

        if(passedReaction != null){
            displayReactionData(passedReaction);
        }


    }

    private void displayReactionData(Reaction reaction){


        twTitle.setText(reaction.getmTitle().toString());
        twDescription.setText(reaction.getmDescription().toString());
        twNumberOfLikes.setText(String.valueOf(reaction.getmNumberLikes()));
        twNumberOfDislikes.setText(String.valueOf(reaction.getmNumberDislikes()));


    }

    private void bindViews(){

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("reactions");

        twTitle = findViewById(R.id.tw_reaction_detail_title);
        twDescription = findViewById(R.id.tw_reaction_detail_description);
        twNumberOfDislikes = findViewById(R.id.tw_num_dislikes);
        twNumberOfLikes = findViewById(R.id.tw_num_likes);
        btnDislike = findViewById(R.id.btn_detail_dislike);
        btnLike = findViewById(R.id.btn_detail_like);

    }



    public void onLikeButtonClicked(View view) {


        Toast.makeText(this, "LIKE BUTTON WAS TAPPED", Toast.LENGTH_SHORT).show();

        int currentRating  = passedReaction.getmNumberLikes();

        Log.d("FIRST", String.valueOf(currentRating));

        int newRating = currentRating + 1 ;

        Log.d("UPDATED", String.valueOf(newRating));

        updatedReaction = new Reaction(
                passedReaction.getmReactionId(),
                passedReaction.getmSituationId(),
                passedReaction.getmTitle(),
                passedReaction.getmDescription(),
                newRating,
                passedReaction.getmNumberDislikes(), passedReaction.getmSituationText());


        mDatabaseRef.child(passedReaction.getmReactionId()).child("mNumberLikes").setValue(updatedReaction.getmNumberLikes());

        twNumberOfLikes.setText(String.valueOf(updatedReaction.getmNumberLikes()));

        passedReaction = updatedReaction;
    }

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


    public void onReadReviewsClicked(View view){

        Intent toDisplayReviews = new Intent(this, DisplayReviewsActivity.class);

        Parcelable p = Parcels.wrap(passedReaction);
        Log.d(TAG, String.valueOf(p == null));

        toDisplayReviews.putExtra(DisplayReviewsActivity.EXTRA_RECEPTION_KEY, Parcels.wrap(passedReaction));
        startActivity(toDisplayReviews);
    }

    public void onWriteReviewClicked(View view){

        Intent toWriteReviewActivity = new Intent(this, WriteReviewActivity.class);
        toWriteReviewActivity.putExtra(WriteReviewActivity.EXTRA_RECEPTION_KEY, Parcels.wrap(passedReaction));
        startActivity(toWriteReviewActivity);
    }


}
