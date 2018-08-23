package edu.mit.urop.playground.tsl.feedit.screens;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import edu.mit.urop.playground.tsl.feedit.R;
import edu.mit.urop.playground.tsl.feedit.adapters.OnReviewClickListener;
import edu.mit.urop.playground.tsl.feedit.adapters.ReviewAdapter;
import edu.mit.urop.playground.tsl.feedit.models.Reaction;
import edu.mit.urop.playground.tsl.feedit.models.Review;

public class DisplayReviewsActivity extends AppCompatActivity implements OnReviewClickListener{

    public static final String EXTRA_RECEPTION_KEY = "LbW9xS4";

    RecyclerView reviewsRecyclerView;
    ReviewAdapter adapter;
    Reaction reaction;
    DatabaseReference mDatabaseRef;
    List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reviews);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("reviews");

        reviews = new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1 );

        reviewsRecyclerView = findViewById(R.id.rw_review_grid_container);
        reviewsRecyclerView.setHasFixedSize(true);
        reviewsRecyclerView.setLayoutManager(layoutManager);

        Intent fromReactionDetailsActivity = getIntent();

        if(fromReactionDetailsActivity != null){

            reaction = Parcels.unwrap(fromReactionDetailsActivity.getParcelableExtra(EXTRA_RECEPTION_KEY));


            //THIS IF STATEMENT WILL BE CHANGED WITH A CHECK FOR WHETHER THE REVIEWS WHOSE REACTION ID
            //MATCH THE REACTION AT HAND ARE 0 OR NOT. THEN SAY NO REVIEWS.
            //MAIN THING IS: REVIEWS DO NOT LIVE IN THE REACTION CLASS ANYMORE, THEY'RE A DIFFERENT TABLE JOINED WITH AN REACTIONID
            //COLUMN.

            Query firebaseQuery = mDatabaseRef.orderByChild("reactionId").equalTo(reaction.getmReactionId());

            firebaseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for(DataSnapshot reviewsSnapShot : dataSnapshot.getChildren()){

                        Review review = reviewsSnapShot.getValue(Review.class);

                        reviews.add(review);

                    }

                    if(reviews.size() == 0){

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                DisplayReviewsActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("There are no reviews yet, would you like to write one?");

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("Write review",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {


                                        Intent toWriteReviewActivity = new Intent(DisplayReviewsActivity.this,
                                                WriteReviewActivity.class);

                                        toWriteReviewActivity.putExtra(WriteReviewActivity.EXTRA_RECEPTION_KEY, Parcels.wrap(reaction));

                                        startActivity(toWriteReviewActivity);
                                    }
                                })
                                .setNegativeButton("Not now",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        //If this button is clicked, cancel the dialog.

                                        dialog.cancel();
                                    }
                                });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                    }





                    adapter = new ReviewAdapter(reviews, DisplayReviewsActivity.this);
                    reviewsRecyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }

   }

    @Override
    public void onShareButtonClicked(int reviewPosition) {



        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, reviews.get(reviewPosition).getReviewText());
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "A valuable insight from Feed-It");
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "Share with..."));

    }
}

