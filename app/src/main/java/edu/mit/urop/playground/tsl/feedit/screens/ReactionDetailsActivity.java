package edu.mit.urop.playground.tsl.feedit.screens;

import android.content.Intent;
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

        Intent fromViewReactionsActivity = getIntent();
        if(fromViewReactionsActivity != null ){


            passedReaction = Parcels.unwrap(fromViewReactionsActivity.getParcelableExtra(EXTRA_RECEPTION_KEY));

            Log.d("NULL", String.valueOf(passedReaction.getTitle()));

        }

        bindViews();

        if(passedReaction != null){
            displayReactionData(passedReaction);
        }


    }

    private void displayReactionData(Reaction reaction){


        twTitle.setText(reaction.getTitle().toString());
        twDescription.setText(reaction.getDescription().toString());
        twNumberOfLikes.setText(String.valueOf(reaction.getNumberLikes()));
        twNumberOfDislikes.setText(String.valueOf(reaction.getNumberDislikes()));


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

        int currentRating  = passedReaction.getNumberLikes();

        Log.d("FIRST", String.valueOf(currentRating));

        int newRating = currentRating + 1 ;

        Log.d("UPDATED", String.valueOf(newRating));

        updatedReaction = new Reaction(
                passedReaction.getReactionId(),
                passedReaction.getSituationId(),
                passedReaction.getTitle(),
                passedReaction.getDescription(),
                newRating,
                passedReaction.getNumberDislikes(), passedReaction.getReviews());


        mDatabaseRef.child(passedReaction.getReactionId()).child("mNumberLikes").setValue(updatedReaction.getNumberLikes());

        twNumberOfLikes.setText(String.valueOf(updatedReaction.getNumberLikes()));

        passedReaction = updatedReaction;
    }

    public void onDislikeButtonClicked(View view) {

        Toast.makeText(this, "DISLIKE BUTTON WAS TAPPED", Toast.LENGTH_SHORT).show();

        int currentRating  = passedReaction.getNumberDislikes();

        Log.d("FIRST", String.valueOf(currentRating));

        int newRating = currentRating + 1 ;

        Log.d("UPDATED", String.valueOf(newRating));

        updatedReaction = new Reaction(
                passedReaction.getReactionId(),
                passedReaction.getSituationId(),
                passedReaction.getTitle(),
                passedReaction.getDescription(),
                passedReaction.getNumberLikes(),
                newRating, passedReaction.getReviews());


        mDatabaseRef.child(passedReaction.getReactionId()).child("mNumberDislikes").setValue(updatedReaction.getNumberDislikes());

        twNumberOfDislikes.setText(String.valueOf(updatedReaction.getNumberDislikes()));

        passedReaction = updatedReaction;

    }



}
