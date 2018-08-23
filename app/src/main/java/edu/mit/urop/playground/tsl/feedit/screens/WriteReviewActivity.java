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

public class WriteReviewActivity extends AppCompatActivity {


    public static final String TAG = WriteReviewActivity.class.getSimpleName();
    public static final String EXTRA_RECEPTION_KEY = "2LSJCNI59";
    EditText etReviewInput;
    Button btnSubmitReview;
    Reaction passedReaction;
    DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        etReviewInput = findViewById(R.id.et_review_input);
        btnSubmitReview = findViewById(R.id.btn_submit_review);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("reviews");

        Intent fromReactionDetailsActivity = getIntent();

        if(fromReactionDetailsActivity != null){

            passedReaction = Parcels.unwrap(fromReactionDetailsActivity.getParcelableExtra(EXTRA_RECEPTION_KEY));


        }

    }


    public void onSubmitReviewClicked(View view){

        String reviewInput = etReviewInput.getText().toString();

        String reviewId = mDatabaseRef.push().getKey();


        Review review = new Review(passedReaction.getmReactionId(), reviewId, reviewInput );


        mDatabaseRef.child(reviewId).setValue(review);


        Toast.makeText(getApplicationContext(), "Your review has been submitted.", Toast.LENGTH_SHORT).show();

        Intent toReactionDetailsActivity = new Intent(this, ReactionDetailsActivity.class);

        toReactionDetailsActivity.putExtra("source",  TAG);

        toReactionDetailsActivity.putExtra(ReactionDetailsActivity.EXTRA_RECEPTION_KEY, Parcels.wrap(passedReaction));

        startActivity(toReactionDetailsActivity);


    }
}
